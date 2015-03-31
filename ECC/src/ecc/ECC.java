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
        this.publicKey = basePoint.multiplication(privateKey);
    }
    
    //Save private key into *.pri file
    public void savePrivateKey(String file) throws FileNotFoundException, IOException{
        byte[] bytes = Long.toHexString(privateKey).getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
    
    //Save public key into *.pub file
    public void savePublicKey(String file) throws IOException{
        byte[] bytes = publicKey.toHexString().getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
    
    //Read private key from *.pri file
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
    
    //Read public key from *.pub file
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
    
    //Encode a character into point using Koblitz' method
    private Point encoding(char c){
        int m = (int) c;
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
    
    //Returns String cipherText in hexadecimal notation
    public String encrypt(String plainText){
        String cipherText = "" + basePoint.toHexString() + " ";
        for (char c : plainText.toCharArray()){
            Point Pm = encoding(c);
            Pm = Pm.addition(basePoint.multiplication(k));
            cipherText += Pm.toHexString() + " ";
        }
        return cipherText;
    }
    
    //Decode a point into character using Koblitz' method
    private char decoding(Point p){
        long m = (p.getX()-1)/k;
        return (char) m;
    }
    
    //Returns plainText String for hecadecimal cipherText String
    public String decrypt(String cipherText){
        String plainText = "";
        String[] splitted = cipherText.split("\\s+");
        basePoint = Point.parsePoint(splitted[0] + " " + splitted[1]);
        for (int i=2; i<=splitted.length-2; i+=2){
            Point p = Point.parsePoint(splitted[i] + " " + splitted[i+1]);
            plainText += decoding(p);
        }
        return plainText;
    }
    
    //Write any string into any file
    public void writeToFile(String str, String file) throws FileNotFoundException, IOException{
        byte[] bytes = str.getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
    
    //Returns a string representation of any file
    public String readFile(String file){
        Path path = Paths.get(file);
        String str = new String();
        try {
            byte[] bytes = Files.readAllBytes(path);
            str = new String(bytes);
        } catch (IOException ex) {
            Logger.getLogger(ECC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }
    
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
