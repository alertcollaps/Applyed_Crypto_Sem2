package Protocols.Guillou_Quisquater;

import java.math.BigInteger;
import java.util.Random;

public class ProtocolMain {
    private static BigInteger one = BigInteger.valueOf(1);
    private static BigInteger minisOne = BigInteger.valueOf(-1);
    public static void main(String[] args) {
        Random rnd = new Random();
        BigInteger p = BigInteger.probablePrime(3, rnd);
        BigInteger q = BigInteger.probablePrime(4, rnd);
        BigInteger N = p.multiply(q);
        BigInteger phi = p.subtract(one).multiply(q.subtract(one)); //(p-1)*(q-1)
        BigInteger u = getRandomU(phi);
        int t = 5;
        BigInteger Ja = getRandomJa(N, phi);
        BigInteger s = evalExp(u, phi);
        BigInteger Sa = evaleSa(Ja, s, N);
        System.out.println("[ProtocolMain]: N = " + N + " = " + p + " * " + q);
        System.out.println("[ProtocolMain]: t = " + t);
        System.out.println("[ProtocolMain]: u = " + u);
        System.out.println("[ProtocolMain]: Sa = " + Sa);
        A claimer = new A(N, u, Ja, Sa);
        B verifyer = new B(t, N, u, claimer);
        verifyer.startProtocol();

        claimer.print();
        verifyer.print();
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
