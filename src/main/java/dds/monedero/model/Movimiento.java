package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private LocalDate fecha;
  private Double monto;

  public Movimiento(LocalDate fecha, Double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public Double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public Boolean fueDepositado(LocalDate fecha) {
    return isDeposito() && esDeLaFecha(fecha);
  }

  public Boolean fueExtraido(LocalDate fecha) {
    return !isDeposito() && esDeLaFecha(fecha);
  }

  public Boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public Boolean isDeposito() {
    return esDeposito;
  }

  public void agregateA(Cuenta cuenta) {
    cuenta.setSaldo(calcularValor(cuenta));
    cuenta.agregarMovimiento(fecha, monto, esDeposito);
  }

  public Double calcularValor(Cuenta cuenta) {
    if (esDeposito) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }



}
