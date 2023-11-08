//Prover



import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class A {
    StringBuffer logs;
    int tt;
    BigInteger N;
    BigInteger p;
    BigInteger q;
    private BigInteger one = BigInteger.valueOf(1);
    private byte[] signat = null;
    private BigInteger a;
    private BigInteger beta;

    private BigInteger u; //public exponent
    private BigInteger R;
    private BigInteger Ja;

    
    String name = "[A]:";
    A(){
        N = BigInteger.valueOf(493);
        init();

    }
    A(BigInteger N, BigInteger p,  BigInteger beta, BigInteger q, BigInteger Ja, int tt){
        this.N = N;
        this.Ja = Ja;

        this.beta = beta;
        this.p = p;
        this.q = q; 
        this.tt = tt;
        init();
    }
    public byte[] getSignat(){
        return signat;
    }

    void init(){
        logs = new StringBuffer();
        a = getRandomA();
        u = beta.modPow(a.negate(), p); //beta^-a mod p
        signat = ProtocolMain.sign(Ja, u); //Sign
        signat[0] = 100; //Error

        logs.append(name + " get public u: " + u);
        logs.append('\n');
    }
    private BigInteger getRandomA(){
        Random rnd = new Random();
        BigInteger out;
        out = new BigInteger(q.bitLength(), rnd).mod(q.subtract(one));

        return out;
    }

    private BigInteger getRandomBigInteger(){
        Random rnd = new Random();
        BigInteger out;
        do {
            out = new BigInteger(q.bitLength(), rnd).mod(q.subtract(one));
        } while (out.compareTo(BigInteger.valueOf(1)) == -1);

        return out;
    }
   
    private BigInteger getPowBigInteger(BigInteger value){
        return value.modPow(R, p);
    }
    public BigInteger getU(){
        return u;
    }
    public BigInteger getJa(){
        return Ja;
    }

    BigInteger transferRSqr(){
        R = getRandomBigInteger();
        logs.append(name + " choose R: " + R);
        logs.append('\n');
        return getPowBigInteger(beta); //R^u mod N

    }


    BigInteger calcY(BigInteger e){
        if (e.bitLength() < 1 || e.bitLength() > tt){
            logs.append(name + "Error e: must be 1 <= e <= 2^" + tt);
            logs.append('\n');
            return null;
        }
        logs.append(name + " get e: " + e);
        logs.append('\n');
        return a.multiply(e).add(R).mod(q);
    }

    public void print(){
        System.out.println(logs);
    }
}