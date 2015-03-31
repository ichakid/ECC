/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.floor;
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
    public String encrypt(String plainText) throws Exception{
        String cipherText = "" + basePoint.toString() + " ";
        for (char c : plainText.toCharArray()){
            Point Pm = encoding(c); Pm.setA(ellipticCurve.getA());
            Pm = Pm.addition(basePoint.multiplication(k));
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
        basePoint = new Point(Long.parseLong(splitted[0]), Long.parseLong(splitted[1]));
        basePoint.setA(ellipticCurve.getA());
        Point temp = basePoint.multiplication(privateKey);
        for (int i=2; i<=splitted.length-2; i+=2){
            Point p = new Point(Long.parseLong(splitted[i]), Long.parseLong(splitted[i+1]));
            Point Pm = p.addition(temp.inverse());
            plainText += decoding(Pm);
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
        // TODO code application logic here
        ECC ecc = new ECC();
        ecc.setEllipticCurve(13);
        ArrayList<Point> GF = ecc.getEllipticGroup();
//        for (Point p : GF){
//            System.out.println("P(" + p.getX() + "," + p.getY() + ")");
//        }
        ecc.setBasePoint(new Point(2, 4));
        ecc.setPrivateKey(5);
        ecc.setPublicKey();
        ecc.setK(5);
        String c = ecc.encrypt("Haloooo");
        System.out.println("Cipher: " + c);
        String p = ecc.decrypt(c);
        System.out.println("Plain: " + p);
    }
    
}
