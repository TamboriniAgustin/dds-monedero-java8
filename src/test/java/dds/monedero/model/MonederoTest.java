package dds.monedero.model;

import org.junit.Before;
import org.junit.Test;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import junit.framework.Assert;

public class MonederoTest {
  private Cuenta cuenta;

  @Before
  public void init() {
    cuenta = new Cuenta();
  }

  @Test
  public void ingresar_monto() {
    cuenta.poner(1500);
    Assert.assertEquals(1500.0, cuenta.getSaldo());
  }

  @Test(expected = MontoNegativoException.class)
  public void ingresar_monto_negativo() {
    cuenta.poner(-1500);
  }

  @Test
  public void depositar_3_veces_correctamente() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    Assert.assertEquals(1500 + 456 + 1900, cuenta.getSaldo(), 0);
  }

  @Test(expected = MaximaCantidadDepositosException.class)
  public void depositar_4_veces() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    cuenta.poner(245);
  }

  @Test(expected = SaldoMenorException.class)
  public void extraccion_mayor_al_saldo() {
    cuenta.setSaldo(90);
    cuenta.sacar(1001);
  }

  @Test(expected = MaximoExtraccionDiarioException.class)
  public void extraccion_superior_a_1000() {
    cuenta.setSaldo(5000);
    cuenta.sacar(1001);
  }

  @Test(expected = MontoNegativoException.class)
  public void extraer_monto_negativo() {
    cuenta.sacar(-500);
  }

}