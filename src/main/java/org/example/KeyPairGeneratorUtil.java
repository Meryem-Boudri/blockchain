package org.example;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KeyPairGeneratorUtil {


    private static final String ALGORITHM = "EC";
    private static final String CURVE = "secp256r1";
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE);
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static PublicKey getPublicKey(KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = keyPair.getPublic();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }


    public static String getPublicKeyAsString(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }


    public static String getPrivateKeyAsString(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
}
