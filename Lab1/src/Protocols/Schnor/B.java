
import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Random;

import Encrypt.Sign;
import Encrypt.Utils;

public class B {
    private int t;
    private int tt;
    private boolean[] e;
    
    private A claimer;
    private PublicKey key;
    private StringBuffer logs;
    private String name = "[B]:";
    private BigInteger u;
    private BigInteger beta;
    private BigInteger zero = BigInteger.valueOf(0);
    private BigInteger two = BigInteger.valueOf(2);
    private BigInteger N;
    private BigInteger p;
    private BigInteger q;
    private BigInteger Ja;
    B(){
        t = 5;
        claimer = new A();
        N = BigInteger.valueOf(493);
        init();
    }
    B(int t, BigInteger N, BigInteger p, BigInteger q, BigInteger beta, A claimer, PublicKey key, int tt){
        this.t = t;
        this.claimer = claimer;
        this.N = N;
        this.beta = beta;
        this.tt = tt;
        this.key = key;
        this.p = p;
        this.q = q;

        
        init();
    }
    private boolean verify(BigInteger y, BigInteger x, BigInteger e){
        if (y.equals(zero)){
            return false;
        }
        BigInteger z = beta.modPow(y, p).multiply(u.modPow(e, p)).mod(p);
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
            out = new BigInteger(tt-1, rnd).mod(N);
        } while (out.compareTo(BigInteger.valueOf(1)) == -1);

        return out;
    }

    void startProtocol(){
        Sign ss = new Sign();
        ss.initSignature();
        u = claimer.getU();
        for (int i = 0; i < t; i++){
            logs.append(name + " round №" + i);
            //Step 1
            BigInteger x = claimer.transferRSqr();
            byte[] signat = claimer.getSignat(); //Sign
            byte[] data = Utils.concatArrays(claimer.getJa().toByteArray(), u.toByteArray());
            if (!ss.verify(data, signat, key)){ //Verify Sign
                logs.append(name + " failed verify sign!");
                logs.append(". Exiting...");
                logs.append('\n');
                return;
            }
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
