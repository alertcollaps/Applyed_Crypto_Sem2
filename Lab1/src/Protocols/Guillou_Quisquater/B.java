package Protocols.Guillou_Quisquater;

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
    private BigInteger Ja;
    B(){
        t = 5;
        claimer = new A();
        N = BigInteger.valueOf(493);
        init();
    }
    B(int t, BigInteger N, BigInteger u, A claimer){
        this.t = t;
        this.claimer = claimer;
        this.N = N;
        this.u = u;
        
        init();
    }
    private boolean verify(BigInteger y, BigInteger x, BigInteger e){
        if (y.equals(zero)){
            return false;
        }
        BigInteger z = Ja.modPow(e, N).multiply(y.modPow(u, N)).mod(N);
        return z.equals(x) && !z.equals(zero);
    }
    void init(){
        logs = new StringBuffer();
        
        Ja = claimer.getJa();
        logs.append(name + " get Ja: " + Ja);
        logs.append('\n');
    }
    private BigInteger getRandomE(){
        Random rnd = new Random();
        BigInteger out;
        do {
            out = new BigInteger(N.bitLength(), rnd).mod(N);
        } while (out.compareTo(BigInteger.valueOf(1)) == -1);

        return out;
    }

    void startProtocol(){
        for (int i = 0; i < t; i++){
            logs.append(name + " round №" + i);
            //Step 1
            BigInteger x = claimer.transferRSqr();
            logs.append(name + " get x: " + x);
            logs.append('\n');
            //Step 2
            BigInteger e = getRandomE();
            BigInteger y = claimer.calcY(e);
            if (!verify(y, x, e)){
                logs.append(name + " failed verify! e: " + e);
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
