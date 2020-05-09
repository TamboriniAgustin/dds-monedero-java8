package dds.monedero.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {
  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();
  private int limiteDepositos = 3;
  private int limiteExtraccion = 1000;

  public Cuenta() {
    saldo = 0;
  }
  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }
  
  public List<Movimiento> getMovimientos() {
	return movimientos;
  }
  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }
  public double getSaldo() {
	return saldo;
  }
  public void setSaldo(double saldo) {
	this.saldo = saldo;
  }
  
  public double getMontoExtraidoA(LocalDate fecha) {
	 return getMovimientos().stream().filter(movimiento -> !movimiento.esDeposito() && movimiento.getFecha().equals(fecha)).mapToDouble(Movimiento::getMonto).sum();
  }
  public int getDepositos(LocalDate fecha) {
	  return getMovimientos().stream().filter(movimiento -> movimiento.esDeposito() && movimiento.getFecha().equals(fecha)).toArray().length;
  }
  
  public void poner(double cuanto) {
    this.validarMontoPositivo(cuanto);
    // Si pasa la validación del monto entonces paso a validar la cantidad de depositos diarios
    this.validarDeposito();
    // Si pasa la validación para depositar, genero los cambios en la cuenta y creo la operación
    this.setSaldo(this.getSaldo() + cuanto);
    this.agregarMovimiento(LocalDate.now(), cuanto, true);
  }
  public void sacar(double cuanto) {
    validarMontoPositivo(cuanto);
    // Si pasa la validación del monto entonces paso a validar la extracción
    validarExtraccion(cuanto);
    // Si pasa la validación para extraer, genero los cambios en la cuenta y creo la operación
    this.setSaldo(this.getSaldo() - cuanto);
    this.agregarMovimiento(LocalDate.now(), cuanto, false);
  }
  
  public void agregarMovimiento(LocalDate fecha, double cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
  }
  
  private void validarMontoPositivo(double monto) {
	if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
	}
  }
  
  private void validarDeposito() {
	  if(this.getDepositos(LocalDate.now()) >= this.limiteDepositos) {
		  throw new MaximaCantidadDepositosException("Ya excedio los " + limiteDepositos + " depositos diarios");
	  }
  }
  
  private boolean montoDisponible(double monto) {
	  return monto <= this.getSaldo();
  }
  private boolean puedeExtraer(double monto) {
	  return (monto + getMontoExtraidoA(LocalDate.now())) <= this.limiteExtraccion;
  }
  private void validarExtraccion(double monto) {
	  if(!this.montoDisponible(monto)) {
		  throw new SaldoMenorException("No puede sacar mas de " + this.getSaldo() + " $");
	  }
	  if(!this.puedeExtraer(monto)) {
		  throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + limiteExtraccion + " diarios");
	  }
  }
}

/* CODE SMELLS IDENTIFICADOS */
// Duplicated method en sacar y poner
// Large class en 'Cuenta' y Large method en sacar() y poner()
// Misplaced method en agregateA() 'Utiliza unicamente la cuenta, no utiliza nada propio de la operación'