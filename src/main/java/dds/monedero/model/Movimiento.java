package dds.monedero.model;
import java.time.LocalDate;

public class Movimiento {
  private LocalDate fecha;
  private double monto;
  private boolean esDeposito;

  public Movimiento(LocalDate fecha, double monto, boolean esDeposito) {
    this.fecha = fecha;
    this.monto = monto;
    this.esDeposito = true;
  }
  
  public double getMonto() {
    return monto;
  }
  public LocalDate getFecha() {
    return fecha;
  }
  
  public boolean esDeposito() {
    return this.esDeposito;
  }
}