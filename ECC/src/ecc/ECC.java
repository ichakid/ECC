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
    
    public void setEllipticCurve(long p){
        this.ellipticCurve.setP(p);
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
    
    public String readFile(String fileToRead){
            String textRead = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileToRead))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                textRead += sCurrentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textRead; 
    }
    
    public void writeFile(String textToWrite, String fileToWrite){
        FileWriter fw = null;
        try {
            File file = new File(fileToWrite);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }   fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(textToWrite);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ECC.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(ECC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
