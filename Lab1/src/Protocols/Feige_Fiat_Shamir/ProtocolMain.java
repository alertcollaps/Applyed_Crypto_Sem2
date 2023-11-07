package Protocols.Feige_Fiat_Shamir;

import java.math.BigInteger;
import java.util.Random;

public class ProtocolMain {
    public static void main(String[] args) {
        Random rnd = new Random();
        BigInteger p = BigInteger.probablePrime(3, rnd);
        BigInteger q = BigInteger.probablePrime(4, rnd);
        BigInteger N = p.multiply(q);
        int k = 5;

        System.out.println("[ProtocolMain]: N = " + N + " = " + p + " * " + q);
        System.out.println("[ProtocolMain]: t = " + k);
        A claimer = new A(N, k);
        B verifyer = new B(k, N, claimer);
        verifyer.startProtocol();

        claimer.print();
        verifyer.print();
    }
}
