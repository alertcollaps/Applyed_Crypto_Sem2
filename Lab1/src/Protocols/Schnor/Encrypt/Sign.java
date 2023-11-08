package Encrypt;

import java.security.*; 
import java.util.*;

public class Sign {
  
    private Signature signature;
    private KeyPair keyPair;
    public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }
    public void initSignature(){
        
        try {
            signature = Signature.getInstance("SHA256WithDSA");
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            keyPair = keyPairGenerator.generateKeyPair();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    public byte[] sign(byte[] data){
        try {
            SecureRandom secureRandom = new SecureRandom();
            

            signature.initSign(keyPair.getPrivate(), secureRandom);
            signature.update(data);
            return signature.sign();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        
    }

    public boolean verify(byte[] data, byte[] signatur, PublicKey key){
        try {
            signature.initVerify(key);
            signature.update(data);
            return signature.verify(signatur);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        byte[] key = new byte[32];
        byte[] data = "hello".getBytes();
        //byte[] hmac = HmacFunc(key, data);
        byte[] enc = null;
        byte[] dec = null;
        try{
            initSignature();
        } catch (Exception e){
            e.printStackTrace();
        }
        
        System.out.println(new String(dec));
    }
}
