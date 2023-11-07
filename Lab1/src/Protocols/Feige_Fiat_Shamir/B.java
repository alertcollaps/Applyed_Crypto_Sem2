package Protocols.Feige_Fiat_Shamir;

import java.math.BigInteger;
import java.util.Random;

public class B {
    private int k;
    private boolean[] e;
    private A claimer;
    private StringBuffer logs;
    private String name = "[B]:";
    private BigInteger[] u;
    private BigInteger zero = BigInteger.valueOf(0);
    private BigInteger two = BigInteger.valueOf(2);
    private BigInteger N;
    B(){
        k = 5;
        claimer = new A();
        N = BigInteger.valueOf(493);
        init();
    }
    B(int k, BigInteger N, A claimer){
        this.k = k;
        this.claimer = claimer;
        this.N = N;
        
        init();
    }
    private boolean verify(BigInteger y, BigInteger x, boolean[] e){
        if (y.equals(zero)){
            return false;
        }
        BigInteger out = BigInteger.valueOf(1); //y^2 mod N
        for (int i = 0; i < k; i++){
            if (e[i]){
                out = out.multiply(u[i]).mod(N);
            }
        }
        out = y.pow(2).multiply(out).mod(N);
        return (out.equals(x) || out.equals(x.negate().mod(N))) && !out.equals(zero);
    }
    void init(){
        logs = new StringBuffer();
        
        Random rnd = new Random();
        e = new boolean[k];
        for (int i = 0; i < k; i++){
            e[i] = rnd.nextBoolean();
        }
        e[rnd.nextInt(k)] = true; //Гарантирует хотя бы одну единицу

        u = claimer.registerU(); //Получаем открытый ключ: u = s^2 mod N
        logs.append(name + " get u: " + u);
        logs.append('\n');
    }

    void startProtocol(){
        
        //Step 1
        BigInteger x = claimer.transferRSqr();
        logs.append(name + " get x: " + x);
        logs.append('\n');
        //Step 2
        BigInteger y = claimer.calcY(e);
        if (!verify(y, x, e)){
            logs.append(name + " failed verify!");
            logs.append(". Exiting...");
            logs.append('\n');
            return;
        }
        
        logs.append(name + "Successful check! End protocol.");
        logs.append('\n');
    }

    public void print(){
        System.out.println(logs);
    }
}
