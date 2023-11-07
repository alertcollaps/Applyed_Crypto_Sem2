//Prover

package Protocols.Fiat_Shamir;

import java.math.BigInteger;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class A {
    StringBuffer logs;
    BigInteger N;
    private BigInteger s;
    private BigInteger u;
    private BigInteger R;
    String name = "[A]:";
    A(){
        N = BigInteger.valueOf(493);
        init();

    }
    A(BigInteger N){
        this.N = N;
        init();
    }

    void init(){
        logs = new StringBuffer();
        
        do  {
            s = getRandomBigInteger();
        } while ( !N.gcd(s).
                        equals( BigInteger.valueOf(1)) );
        u = getPowBigInteger(s);
        logs.append(name + " choose s: " + s);
        logs.append('\n');
    }
    private BigInteger getRandomBigInteger(){
        Random rnd = new Random();
        return BigInteger.valueOf(rnd.nextInt(N.intValue()-1) + 1);
    }
    private BigInteger getPowBigInteger(BigInteger value){
        return value.modPow(BigInteger.valueOf(2), N);
    }
    public BigInteger getU(){
        return u;
    }

    BigInteger transferRSqr(){
        R = getRandomBigInteger();
        logs.append(name + " choose R: " + R);
        logs.append('\n');
        return getPowBigInteger(R);

    }

    BigInteger calcY(boolean e){
        logs.append(name + " get e: " + e);
        logs.append('\n');
        if (!e){
            return R;
        } else {
            //return R; //Error variant
            return R.multiply(s).mod(N);
        }
    }

    public void print(){
        System.out.println(logs);
    }
}