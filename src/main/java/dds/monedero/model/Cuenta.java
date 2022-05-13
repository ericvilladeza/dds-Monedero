package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private Double saldo;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extraccion = new ArrayList<>();

  public Cuenta() {
    saldo = 0.0;
  }

  public Cuenta(Double montoInicial) {
    saldo = montoInicial;
  }

  public List<Extraccion> getExtraccion() {
    return extraccion;
  }

  public List<Deposito> getDepositos() {
    return depositos;
  }

  public void setExtraccion(List<Extraccion> extraccion) {
    this.extraccion = extraccion;
  }

  public void setDepositos(List<Deposito> depositos) {
    this.depositos = depositos;
  }

  public void poner(Double cuanto) {
    valorMayorQue0 (cuanto);
    depositoDisponible();

    this.depositos.add(new Deposito(LocalDate.now(), cuanto));
    setSaldo(getSaldo()+cuanto);
  }

  public void sacar(Double cuanto) {
    valorMayorQue0 (cuanto);
    platitaDisponible (cuanto);
    limite1000(cuanto);

    this.extraccion.add(new Extraccion(LocalDate.now(), cuanto));
    setSaldo(getSaldo()-cuanto);
  }


  public Double getMontoExtraidoA(LocalDate fecha) {
    return getExtraccion().stream()
        .filter(extraccion ->  extraccion.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }


  public Double getSaldo() {
    return saldo;
  }

  public void setSaldo(Double saldo) {
    this.saldo = saldo;
  }

  public long cantidadDeDepositos(){
    return this.depositos.size();
  }


  public void depositoDisponible(){
    if (cantidadDeDepositos() >= 3) {
    throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
  }
  }


  public void valorMayorQue0 (Double cuanto){
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void platitaDisponible (Double cuanto){
    if (this.getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void limite1000(Double cuanto) {
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
              + " diarios, límite: " + limite);
    }
  }



}
