package Protocols.Fiat_Shamir;

import java.math.BigInteger;
import java.util.Random;

public class B {
    private int t;
    private boolean[] e;
    private A claimer;
    private StringBuffer logs;
    private String name = "[B]:";
    private BigInteger u;
    private BigInteger zero = BigInteger.valueOf(0);
    private BigInteger two = BigInteger.valueOf(2);
    private BigInteger N;
    B(){
        t = 5;
        claimer = new A();
        N = BigInteger.valueOf(493);
        init();
    }
    B(int t, BigInteger N, A claimer){
        this.t = t;
        this.claimer = claimer;
        this.N = N;
        
        init();
    }
    private boolean verify(BigInteger y, BigInteger x, boolean e){
        if (y.equals(zero)){
            return false;
        }
        BigInteger left = y.modPow(two, N); //y^2 mod N
        if (e){
            BigInteger right = x.multiply(u).mod(N); //x*u mod N
            return left.equals(right);
        } else {
            return left.equals(x);
        }
    }
    void init(){
        logs = new StringBuffer();
        
        Random rnd = new Random();
        e = new boolean[t];
        for (int i = 0; i < t; i++){
            e[i] = rnd.nextBoolean();
        }
        e[rnd.nextInt(t)] = true; //Гарантирует хотя бы одну единицу

        u = claimer.getU(); //Получаем открытый ключ: u = s^2 mod N
        logs.append(name + " get u: " + u);
        logs.append('\n');
    }

    void startProtocol(){
        for (int i = 0; i < t; i++){
            //Step 1
            BigInteger x = claimer.transferRSqr();
            logs.append(name + " get x: " + x);
            logs.append('\n');
            //Step 2
            BigInteger y = claimer.calcY(e[i]);
            if (!verify(y, x, e[i])){
                logs.append(name + " failed verify! e: " + e[i]);
                logs.append(". Exiting...");
                logs.append('\n');
                return;
            }
        }
        logs.append(name + "Successful check! End protocol.");
        logs.append('\n');
    }

    public void print(){
        System.out.println(logs);
    }
}
