/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecc;

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
    
    public byte[] decrypt(String cipherText){
        byte[] bytes;
        int j = 0;
        String[] splitted = cipherText.split("\\s+");
        basePoint = Point.parsePoint(splitted[0] + " " + splitted[1]);
        for (int i=2; i<=splitted.length-2; i+=2){
            Point p = Point.parsePoint(splitted[i] + " " + splitted[i+1]);
            bytes[j] = decoding(p);
            j++;
        }
        return bytes;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Point p = new Point(2, 20);
        Point q = new Point(1, 0);
//        Point r = p.iteration(3);
//        Point r = p.penjumlahan(p);
//        Point r = p.perkalian(9);
//        System.out.println("R(" + r.getX() + "," + r.getY() + ")");
        System.out.println(p.toHexString());
    }
    
}
