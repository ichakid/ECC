/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ECC {
    private Curve ellipticCurve;
    private Point basePoint;
    private long privateKey;
    private Point publicKey;
    private long k;
    
    
    public void setEllipticCurve(long p){
        this.ellipticCurve.setP(p);
        this.ellipticCurve.setEllipticGrup();
    }
    
    public Point getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(Point basePoint) {
        this.basePoint = basePoint;
    }

    public long getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(long x) {
        this.privateKey = x;
    }

    public Point getPublicKey() {
        return publicKey;
    }

    public void setPublicKey() {
        this.publicKey = basePoint.perkalian(privateKey);
    }
    
    private Point koblitzAlgo(byte b){
        long m = (int) b;
        boolean found = false;
        long x = m * k + 1;
        long y = -1;
        long upperBound = m * k + k - 1;
        while ((!found) && (x < upperBound)){
            y = ellipticCurve.getY(x);
            if (y != -1){
                found = true;
            } else {
                x++;
            }
        }
        return new Point(x, y);
    }
    
    public String encrypt(byte[] bytes){
        String cipherText = "" + basePoint.pointToHex() + " ";
        for (byte b : bytes){
            Point Pm = koblitzAlgo(b);
            Pm = Pm.penjumlahan(basePoint.perkalian(k));
            cipherText += Pm.pointToHex() + " ";
        }
        return cipherText;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Point p = new Point(2, 8);
        Point q = new Point(1, 0);
//        Point r = p.iteration(3);
//        Point r = p.penjumlahan(p);
        Point r = p.perkalian(9);
        System.out.println("R(" + r.getX() + "," + r.getY() + ")");
    }
    
}
