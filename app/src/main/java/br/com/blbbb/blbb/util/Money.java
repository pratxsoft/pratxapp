package br.com.blbbb.blbb.util;

import java.math.BigDecimal;

public final class Money {

	private BigDecimal money;

	public Money(String pMoney) {
		// crio o bigdecimal
		this.money = new BigDecimal(pMoney);

		// configura o bigdecimal
		this.money = this.configurar(this.money);
	}

	public Money(BigDecimal pMoney) {
		// crio o bigdecimal
		this.money = pMoney;

		// configura o bigdecimal
		this.money = this.configurar(this.money);
	}

	private BigDecimal configurar(BigDecimal pMoney) {
		return pMoney.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
	/**
	 * Verifica se o valor e igual a outro do parametro
	 * @param pValor
	 * @return boolean
	 */
	public boolean eIgualA(BigDecimal pValor){
		if (this.money.compareTo(pValor) == 1){  
            return true;
        }
		return false;
	}
	
	/**
	 * Verifica se o valor e maior que outro do parametro
	 * @param pValor
	 * @return boolean
	 */
	public boolean eMaiorQue(BigDecimal pValor){
		if (this.money.compareTo(pValor) > 0){  
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se o valor e maior ou igual que outro do parametro
	 * @param pValor
	 * @return boolean
	 */
	public boolean eMaiorIgualQue(BigDecimal pValor){
		if (this.money.compareTo(pValor) >= 0){  
			return true;
		}
		return false;
	}

}
