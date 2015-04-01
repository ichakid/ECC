/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public ECC() {
        this.ellipticCurve = new Curve();
        this.basePoint = new Point();
        this.privateKey = 1;
        this.publicKey = new Point();
        this.k = 1;
    }
          
    public ArrayList<Point> getEllipticGroup(){
        return ellipticCurve.ellipticGroup;
    }
    
    public void setEllipticCurve(long p){
        this.ellipticCurve.setP(p);
        this.ellipticCurve.setEllipticGroup();
    }
    
    public Point getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(Point basePoint) {
        basePoint.setA(ellipticCurve.getA());
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
        basePoint.setA(ellipticCurve.getA());
        this.publicKey = basePoint.multiplication(privateKey);
        publicKey.setA(ellipticCurve.getA());
    }

    public long getK() {
        return k;
    }

    public void setK(long k) {
        this.k = k;
    }
    
    //Save private key into *.pri file
    public void savePrivateKey(String file) throws FileNotFoundException, IOException{
        byte[] bytes = Long.toString(privateKey).getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }
    
    //Save public key into *.pub file
    public void savePublicKey(String file) throws IOException{
        byte[] bytes = publicKey.toString().getBytes();
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
            String str = new String(bytes);
            privateKey = Long.parseLong(str);
        } catch (IOException ex) {
            Logger.getLogger(ECC.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    //Read public key from *.pub file
    public void readPublicKey(String file){
        Path path = Paths.get(file);
        try {
            byte[] bytes = Files.readAllBytes(path);
            String str = new String(bytes);
            String[] splitted = str.split("\\s+");
            publicKey = new Point(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
            publicKey.setA(ellipticCurve.getA());
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
        Point p = new Point(x, y);
        p.setA(ellipticCurve.getA());
        return p;
    }
    
    //Returns String cipherText in hexadecimal notation
    public String encrypt(String plainText) throws Exception{
        basePoint.setA(ellipticCurve.getA());
        Point kB = basePoint.multiplication(k);
        String cipherText = "" + kB.toString() + " ";
        for (char c : plainText.toCharArray()){
            Point Pm = encoding(c); Pm.setA(ellipticCurve.getA());
            Pm = Pm.addition(publicKey.multiplication(k));
            cipherText += Pm.toString() + " ";
        }
        return getHexString(cipherText.getBytes());
    }
    
    //Decode a point into character using Koblitz' method
    private char decoding(Point p){
        long m = (p.getX()-1)/k;
        return (char) m;
    }
    
    //Returns plainText String for hexadecimal cipherText String
    public String decrypt(String cipherText){
        String plainText = "";
        String cipherString = new String(hexStringToByteArray(cipherText));
        String[] splitted = cipherString.split("\\s+");
        Point kB = new Point(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
        kB.setA(ellipticCurve.getA());
        Point temp = kB.multiplication(privateKey);
        temp.setA(ellipticCurve.getA());
        for (int i=2; i<=splitted.length-2; i+=2){
            Point p = new Point(Long.parseLong(splitted[i]), Long.parseLong(splitted[i+1]));
            p.setA(ellipticCurve.getA());
            Point Pm = p.addition(temp.inverse());
            Pm.setA(ellipticCurve.getA());
            plainText += decoding(Pm);
        }
        return plainText;
    }
    
    //Write any string into any file
    public void writeToFile(String str, String file) throws FileNotFoundException, IOException{
        byte[] bytes = str.getBytes();
        FileOutputStream stream = new FileOutputStream(new File(file));
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
    
    private String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result +=
                Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
    
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        ECC ecc = new ECC();
        ecc.setEllipticCurve(131);
        ArrayList<Point> GF = ecc.getEllipticGroup();
//        for (Point p : GF){
//            System.out.println("P(" + p.getX() + "," + p.getY() + ")");
//        }
        ecc.setBasePoint(new Point(0, 55));
        ecc.setPrivateKey(100);
        ecc.setPublicKey();
        Point p = ecc.getPublicKey();
        System.out.println("P(" + p.getX() + "," + p.getY() + ")");
        ecc.setK(25);
        String plain = ecc.readFile("F://grafik.txt");
        long startTime = System.currentTimeMillis();
        String cipher = ecc.encrypt(plain);
        long endTime = System.currentTimeMillis();
        System.out.println("Encryption took " + (endTime - startTime) + " milliseconds");
        ecc.writeToFile(cipher, "F://graf.txt");
        startTime = System.currentTimeMillis();
        plain = ecc.decrypt(cipher);
        endTime = System.currentTimeMillis();
        System.out.println("Decryption took " + (endTime - startTime) + " milliseconds");
        ecc.writeToFile(plain, "F://graf2.txt");
    }
    
}
