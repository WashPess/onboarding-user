package com.onboarding.user.onboardinguser.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
    
	private static final String ALGO = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_LENGTH = 128;

    private byte[] salt;
    private SecretKeyFactory mFactory;

	public String getSalt() {
		Base64.Encoder enc = Base64.getEncoder();
		return enc.encodeToString(this.salt);
	}

	public byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		this.salt = new byte[16];
		random.nextBytes(this.salt);
		return this.salt;
	}

    public String hash(String password, String salt) {
        SecretKeyFactory factory = this.getFactory();

		Base64.Decoder dec = Base64.getDecoder();
		this.salt = dec.decode(salt);

        if (factory != null) {
            try {
                KeySpec spec = new PBEKeySpec(password.toCharArray(), this.salt, PasswordHasher.ITERATION_COUNT, PasswordHasher.KEY_LENGTH);
                byte[] hash = factory.generateSecret(spec).getEncoded();
				Base64.Encoder enc = Base64.getEncoder();
				return enc.encodeToString(hash);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

	public boolean verify(String password, String salt, String passwordDatabase) {
        String hashedPassword = this.hash(password, salt);
        if (hashedPassword == null) {
            // Log fail result
            return false;
        }
        return password.equals(passwordDatabase);
    }

    private SecretKeyFactory getFactory() {
        if (mFactory == null) {
            try {
                mFactory = SecretKeyFactory.getInstance(PasswordHasher.ALGO);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return mFactory;
    }
}
