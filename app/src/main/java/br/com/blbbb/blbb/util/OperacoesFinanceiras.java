package com.br.util;

import java.math.BigDecimal;

public class OperacoesFinanceiras {

	public BigDecimal somar(BigDecimal pNum1, BigDecimal pNum2){
		return pNum1.add(pNum2);
	}
	
	public BigDecimal subtrair(BigDecimal pNum1, BigDecimal pNum2){
		return pNum1.subtract(pNum2);
	}
	
	public BigDecimal multiplicar(BigDecimal pNum1, BigDecimal pNum2){
		return pNum1.multiply(pNum2);
	}
	
	public BigDecimal dividir(BigDecimal pNum1, BigDecimal pNum2){
		return pNum1.divide(pNum2);
	}
	
	
}
