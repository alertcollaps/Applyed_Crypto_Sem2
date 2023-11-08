//Prover

package Protocols.Feige_Fiat_Shamir;

import java.math.BigInteger;
import java.sql.Time;
import java.util.Random;

import javax.management.RuntimeErrorException;

public class A {
    StringBuffer logs;
    BigInteger N;
    private int k;
    private BigInteger[] s;
    private boolean[] b;
    private BigInteger[] u;
    private BigInteger R;
    private BigInteger minusOne = BigInteger.valueOf(-1);
    String name = "[A]:";
    A(){
        N = BigInteger.valueOf(493);
        k = 7;
        init();

    }
    A(BigInteger N, int k){
        this.N = N;
        this.k = k;
        init();
    }

    void init(){
        logs = new StringBuffer();
        Random rnd = new Random();
        b = new boolean[k];
        u = new BigInteger[k];
        s = new BigInteger[k];
        for (int i = 0; i < k; i++){
            b[i] = rnd.nextBoolean(); //Выбор b_i

            do  {
                s[i] = getRandomBigInteger(); //Выбор s_i coprome with N
            } while ( !N.gcd(s[i]).
                        equals( BigInteger.valueOf(1)) );
            
            u[i] = getPowBigInteger(s[i], b[i]); //Вычисление u_i = (-1)^b_i * {s_i}^2...
            logs.append(name + " choose s[" + i + "] = " + s[i]);
            logs.append('\n');
            logs.append(name + " choose b[" + i + "] = " + b[i]);
            logs.append('\n');
            logs.append(name + " evaluate u[" + i + "] = " + u[i]);
            logs.append('\n');
        }
    }
    private BigInteger getRandomBigInteger(){
        Random rnd = new Random();
        return BigInteger.valueOf(rnd.nextInt(N.intValue()-1) + 1);
    }
    private boolean getRandomBool(){
        Random rnd = new Random(System.currentTimeMillis());
        return rnd.nextBoolean();
    }
    private BigInteger getPowBigInteger(BigInteger s, boolean b){
        BigInteger right = s.pow(2).modPow(minusOne, N); //(s^2)^-1 mod N
        if (b){
            return minusOne.multiply(right).mod(N);
        }
        return right;
    }
    private BigInteger computeX(BigInteger R, boolean b){
        BigInteger right = R.pow(2).mod(N); //(R^2) mod N
        if (b){
            return minusOne.multiply(right).mod(N); //-1*(R^2) mod N
        }
        return right; //(R^2) mod N
    }
    public BigInteger[] registerU(){
        return u;
    }
    

    BigInteger transferRSqr(){
        R = getRandomBigInteger();
        logs.append(name + " choose R: " + R);
        logs.append('\n');
        boolean b = getRandomBool();
        logs.append(name + " choose b: " + b);
        logs.append('\n');
        return computeX(R, b);

    }

    BigInteger calcY(boolean[] e){
        logs.append(name + " get e: " + e);
        logs.append('\n');
        
        BigInteger out = BigInteger.valueOf(1);
        for (int i = 0; i < k; i++){
            if (e[i]){
                out = out.multiply(s[i]).mod(N);
                //out = out.multiply(s[i]).mod(N).subtract(minusOne); //Error
            }
        }
        out = out.multiply(R).mod(N);
        return out;
    }

    public void print(){
        System.out.println(logs);
    }
}