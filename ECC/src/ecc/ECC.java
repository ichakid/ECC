/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private long size;
    
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
    
    public void savePrivateKey(String file) throws FileNotFoundException, IOException{
        byte[] bytes = Long.toHexString(privateKey).getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
    
    public void savePublicKey(String file) throws IOException{
        byte[] bytes = publicKey.toHexString().getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
    
    public void readPrivateKey(String file){
        Path path = Paths.get(file);
        try {
            byte[] bytes = Files.readAllBytes(path);
            String hex = new String(bytes);
            privateKey = Long.parseLong(hex, 16);
        } catch (IOException ex) {
            Logger.getLogger(ECC.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void readPublicKey(String file){
        Path path = Paths.get(file);
        try {
            byte[] bytes = Files.readAllBytes(path);
            String hex = new String(bytes);
            publicKey = Point.parsePoint(hex);
        } catch (IOException ex) {
            Logger.getLogger(ECC.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private Point encoding(byte b){
        int m = (int) b;
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
        String cipherText = "" + basePoint.toHexString() + " ";
        for (byte b : bytes){
            Point Pm = encoding(b);
            Pm = Pm.penjumlahan(basePoint.perkalian(k));
            cipherText += Pm.toHexString() + " ";
        }
        return cipherText;
    }
    
    private byte decoding(Point p){
        long m = (p.getX()-1)/k;
        return (byte) m;
    }
    
//    public byte[] decrypt(String cipherText){
//        byte[] bytes;
//        int j = 0;
//        String[] splitted = cipherText.split("\\s+");
//        basePoint = Point.parsePoint(splitted[0] + " " + splitted[1]);
//        for (int i=2; i<=splitted.length-2; i+=2){
//            Point p = Point.parsePoint(splitted[i] + " " + splitted[i+1]);
//            bytes[j] = decoding(p);
//            j++;
//        }
//        return bytes;
//    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Point p = new Point(2, 20);
        Point q = new Point(1, 0);
//        Point r = p.iteration(3);
//        Point r = p.penjumlahan(p);
//        Point r = p.perkalian(9);
//        System.out.println("R(" + r.getX() + "," + r.getY() + ")");
//        System.out.println(p.toHexString());
        ECC ecc = new ECC();
        ecc.setPrivateKey(1);
        ecc.setBasePoint(p);
        ecc.setPublicKey();
        q = ecc.getPublicKey();
        System.out.println("Pub(" + q.getX() + "," + q.getY() + ")");
        ecc.savePublicKey("F://key.pub");
        ecc.readPublicKey("F://key.pub");
        Point r = ecc.getPublicKey();
        System.out.println("Pub(" + r.getX() + "," + r.getY() + ")");
        ecc.savePrivateKey("F://keypri.pri");
        ecc.readPrivateKey("F://keypri.pri");
        System.out.println(ecc.getPrivateKey());
    }
    
}
