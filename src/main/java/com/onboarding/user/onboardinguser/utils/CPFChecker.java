package com.onboarding.user.onboardinguser.utils;

import java.util.InputMismatchException;

public class CPFChecker {
	
	private CPFChecker() {}

	public static boolean isValid(String cpf) {
		try {

			cpf = Document.clear(cpf);
			
			if (
				cpf.equals("00000000000")
				|| cpf.equals("11111111111")
				|| cpf.equals("22222222222")
				|| cpf.equals("33333333333")
				|| cpf.equals("44444444444")
				|| cpf.equals("55555555555")
				|| cpf.equals("66666666666")
				|| cpf.equals("77777777777")
				|| cpf.equals("88888888888")
				|| cpf.equals("99999999999")
				|| (cpf.length() != 11)
			) {
				return false;
			}

			char dig10;
			char dig11;
			int sm;
			int i;
			int r;
			int weight;
	
			// Calculo do 1o. Digito Verificador
			sm = 0; // vriavel auxiliar - acumulador
			weight = 10;
			
			// converte o i-esimo caractere do cpf em um numero:
			// por exemplo, transforma o caractere "0" no inteiro 0
			// (48 eh a posicao de "0" na tabela ASCII)
			for (i=0; i<9; i++) {
				sm = sm + ((cpf.charAt(i) - 48) * weight);
				weight = weight - 1;
			}

			r = 11 - (sm % 11);
			dig10 = '0';
			if (r != 10 && r != 11) {
				dig10 = (char)(r + 48); 
			}

			// Calculo do 2o. Digito Verificador
			sm = 0;
			weight = 11;
			for(i=0; i<10; i++) {
				sm = sm + ((cpf.charAt(i) - 48) * weight);
				weight = weight - 1;
			}

			r = 11 - (sm % 11);
			dig11 = '0';
			if (r != 10 && r != 11) {
				dig11 = (char)(r + 48);
			}

			return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
		} catch (InputMismatchException erro) {
			return false;
		}
	}

	public static String mask(String cpf) {
		return(cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11));
	}

	public static String unmask(String cpf) {
		return Document.clear(cpf);
	}

	
    
}
