package com.onboarding.user.onboardinguser.utils;

import java.util.InputMismatchException;

public class CPF {

	public static boolean isCPF(String cpf) {
		try {
			
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

			char dig10, dig11;
        	int sm, i, r, peso;
	
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			
			// converte o i-esimo caractere do cpf em um numero:
			// por exemplo, transforma o caractere "0" no inteiro 0
			// (48 eh a posicao de "0" na tabela ASCII)
			for (i=0; i<9; i++) {
				sm = sm + ((cpf.charAt(i) - 48) * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			dig10 = '0';
			if (!((r == 10) || (r == 11))) {
				dig10 = (char)(r + 48); 
			}

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for(i=0; i<10; i++) {
				sm = sm + ((cpf.charAt(i) - 48) * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			dig11 = '0';
			if (!((r == 10) || (r == 11))) {
				dig11 = (char)(r + 48);
			}

			if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))){
				return true;
			}
				
			return false;
		} catch (InputMismatchException erro) {
			return false;
		}
	}

	public static String imprimeCPF(String cpf) {
		return(cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." +
		cpf.substring(6, 9) + "-" + cpf.substring(9, 11));
	}
    
}
