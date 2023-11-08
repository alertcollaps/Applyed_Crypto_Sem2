//Prover

package Protocols.Guillou_Quisquater;

import java.math.BigInteger;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class A {
    StringBuffer logs;
    BigInteger N;
    private BigInteger sA;
    private BigInteger u; //public exponent
    private BigInteger R;
    private BigInteger Ja;
    String name = "[A]:";
    A(){
        N = BigInteger.valueOf(493);
        init();

    }
    A(BigInteger N, BigInteger u, BigInteger Ja, BigInteger sA){
        this.N = N;
        this.u = u;
        this.Ja = Ja;
        this.sA = sA;
        init();
    }

    void init(){
        logs = new StringBuffer();
        
        logs.append(name + " get public u: " + u);
        logs.append('\n');
    }
    private BigInteger getRandomBigInteger(){
        Random rnd = new Random();
        return BigInteger.valueOf(rnd.nextInt(N.intValue()-1) + 1);
    }
    private BigInteger getPowBigInteger(BigInteger value){
        return value.modPow(u, N);
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
        return getPowBigInteger(R); //R^u mod N

    }


    BigInteger calcY(BigInteger e){
        logs.append(name + " get e: " + e);
        logs.append('\n');
        //return R.multiply(sA.modPow(e, N)).mod(N);
        return R.multiply(sA.modPow(e.subtract(BigInteger.valueOf(2)), N)).mod(N); //Error
    }

    public void print(){
        System.out.println(logs);
    }
}