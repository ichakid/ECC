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
