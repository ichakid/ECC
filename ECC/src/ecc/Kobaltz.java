/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tucil.kripto.pkg3;

import static java.lang.Math.sqrt;

/**
 *
 * @author asus
 */
public class Kobaltz {

    /**
     * @param args the command line arguments
     */
    
    private long a;
    private long b;
    private long p;
    private long n;
    private int k;
    
    public int encode (char c) {
        int temp = (int) c;
        int result;
        
        if (46 < temp && temp < 58) {
            result = ((int) c - 48);
        }
        else {
            result = ((int) c - 87);
        }
        
        return result;
    } 
    
    public int[] koblitzAlgo(char c) {
        int [] point = new int[2];
        
        int m = encode (c);
        
        int x = m*k + 4;
        long value = x*x*x + a*x + b;
        long modValue = value % p;
        
        for(int j = 1; j < p-1; j++) {
            int y = j;
            long ySquare = y*y;
            if((ySquare % p) == modValue) {
                System.out.println(ySquare);
                point[0] = x;
                point[1] = y;
            }
        }
        
        return point;
    }

    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public long getB() {
        return b;
    }

    public void setB(long b) {
        this.b = b;
    }

    public long getP() {
        return p;
    }

    public void setP(long p) {
        this.p = p;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }
    
    public static void main(String[] args) {
        Kobaltz K = new Kobaltz();
        K.setA(-1);
        K.setB(188);
        K.setP(751);
        K.setK(20);
        String pesan = "B";
        pesan = pesan.toLowerCase();
        char[] m = pesan.toCharArray();
        
        int[] p = K.koblitzAlgo(m[0]);
        System.out.println(p[0]);
        System.out.println(p[1]);
        
//        for(int i = 0; i < m.length; i++) {
//            System.out.println(K.encode(m[i]));
//        }
    }
    
}
