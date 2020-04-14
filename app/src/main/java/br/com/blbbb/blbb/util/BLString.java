package com.br.util;

public class BLString {

	/**
	 * Altera as , por - de uma String
	 * @param pString
	 * @return String
	 */
	public String virgulaToTraco(String pString){
		if(pString != null){
			pString = pString.replace(',', '-');
		}
		return pString;
	}
	
	/**
	 * Altera os - por , de uma String
	 * @param pString
	 * @return String
	 */
	public String tracoToVirgula(String pString){
		if(pString != null){
			pString = pString.replace('-', ',');
		}
		return pString;
	}
}
