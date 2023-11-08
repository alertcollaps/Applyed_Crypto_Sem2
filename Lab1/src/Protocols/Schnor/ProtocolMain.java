

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Random;

import javax.print.attribute.standard.Sides;

import Encrypt.Sign;
import Encrypt.Utils;

public class ProtocolMain {
    private static BigInteger alpha = BigInteger.valueOf(2);
    private static BigInteger beta;
    private static BigInteger zero = BigInteger.valueOf(0);
    private static BigInteger one = BigInteger.valueOf(1);
    private static BigInteger minisOne = BigInteger.valueOf(-1);
    private static BigInteger P;
    private static BigInteger Q;
    private static Sign signat;
    public static void main(String[] args) {
        Random rnd = new Random();
        signat = new Sign();
        signat.initSignature();
        PublicKey pbKey = signat.getPublicKey();
        
        
        setPQ();

        
        BigInteger N = P.multiply(Q);
        BigInteger phi = P.subtract(one).multiply(Q.subtract(one)); //(p-1)*(q-1)
        BigInteger u = getRandomU(phi);
        int t = 5;
        int tt = 100;
        BigInteger Ja = getRandomJa(N, phi);
        BigInteger s = evalExp(u, phi);
        BigInteger Sa = evaleSa(Ja, s, N);
        System.out.println("[ProtocolMain]: N = " + N + " = " + P + " * " + Q);
        System.out.println("[ProtocolMain]: t = " + t);
        System.out.println("[ProtocolMain]: u = " + u);
        System.out.println("[ProtocolMain]: Sa = " + Sa);
        A claimer = new A(N, P, beta, Q, Ja, tt);
        B verifyer = new B(t, N, P, Q, beta, claimer, pbKey, tt);
        verifyer.startProtocol();

        claimer.print();
        verifyer.print();
        
    }
    public static byte[] sign(BigInteger Ja, BigInteger u){
        byte[] data = Utils.concatArrays(Ja.toByteArray(), u.toByteArray());
        return signat.sign(data);
    }
    
    private static void setPQ(){
        Random rnd = new Random();
        BigInteger d = BigInteger.probablePrime(900, rnd);
        BigInteger q = BigInteger.probablePrime(160, rnd);
        while (!d.multiply(q).add(one).isProbablePrime(100)){
            d = d.add(one);
        }
        P = d.multiply(q).add(one);
        Q = q;
        System.out.println("[ProtocolMain]: p:\t" + P + "\n[ProtocolMain]: q:\t" + Q);
        beta = alpha.modPow(d, P);
        System.out.println("[ProtocolMain]: beta:\t" + beta);
        System.out.println("[ProtocolMain]: beta^q mod p: " + beta.modPow(q, P));
        

    }
    private static BigInteger evaleSa(BigInteger Ja, BigInteger s, BigInteger N){
        return Ja.modPow(s.negate(), N);
    }
    private static BigInteger evalExp(BigInteger u, BigInteger phi){
        BigInteger s = u.modPow(minisOne, phi); //s^-1 mod phi
        return s;
    }
    private static BigInteger getRandomJa(BigInteger N, BigInteger phi){
        Random rnd = new Random();
        BigInteger out;
        do {
            out = new BigInteger(N.bitLength(), rnd).mod(N);
        } while (out.compareTo(BigInteger.valueOf(2)) == -1 || !out.gcd(phi).equals(one));

        return out;
    }
    private static BigInteger getRandomU(BigInteger phi){
        Random rnd = new Random();
        BigInteger out;
        do {
            out = new BigInteger(phi.bitLength(), rnd).mod(phi);
        } while (out.compareTo(BigInteger.valueOf(3)) == -1 || !out.gcd(phi).equals(one));

        return out;
    }
}
