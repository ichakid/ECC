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
public class Point {
    private long x;
    private long y;
    private long a;
    public static Point O = new Point(Long.MAX_VALUE, Long.MAX_VALUE);

    public Point() {
        this.x = 0;
        this.y = 0; 
    }
    
    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getA() {
        return a;
    }

    public void setA(long a) {
        this.a = a;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
    
    public Point copy(){
        Point r = new Point(this.x, this.y);
        return r;
    }
    
    //Mengembalikan titik inverse
    public Point inverse(){
        Point r = new Point(this.x, -this.y);
        return r;
    }
    
    //Mengembalikan titik hasil penjumlahan sebuah titik pada dirinya sendiri
    public Point penjumlahan(Point q){
        Point r = new Point();
        if (q == O){
            return this.copy();
        } else if (this == O){
            return q;
        } else if (this.inverse() == q){
            return O;
        } else if (this.x == q.getX()){
            return O;
        } else {
            long lambda = (this.y - q.getY())/(this.x - q.getX());  //Menghitung gradien garis
            long _x = lambda * lambda - this.x - q.getX();
            long _y = lambda * (this.x - _x) - this.y;
            r.setX(_x);
            r.setY(_y);
            return r;
        }
    }
    
    //Mengembalikan titik hasil penjumlahan sebuah titik pada dirinya sendiri
    public Point penggandaan(){
        if (this.y == 0){
            return O;
        } else {
            Point r = new Point();
            long lambda = (3 * this.x * this.x + this.a)/(2 * this.y);  //Menghitung gradien garis
            long _x = lambda * lambda - 2 * this.x;
            long _y = lambda * (this.x - _x) - this.y;
            r.setX(_x);
            r.setY(_y);            
            return r;
        }
    }
    
    //Mengembalikan titik hasil penjumlahan sebuah titik sebanyak k-1 kali terhadap dirinya sendiri
    public Point pelelaran(long k){
        Point r = this.copy();
        for (long i=1; i<k-1; i++){
            r.penjumlahan(this);
        }
        return r;
    }
    
    // Perkalian titik diperoleh dengan perulangan dua operasi dasar
    // kurva eliptik yang sudah dijelaskan:
    // 1. Penjumlahan titik (P + Q = R)
    // 2. Penggandaan titik (2P = R)
    public Point perkalian(long k){
        Point r = new Point();
        if (k == 1){
            return this.copy();
        } else {
            r = this.copy().perkalian(k/2);
            r = r.penggandaan();
            if (k % 2 == 1){
                r = r.penjumlahan(this);
            }
            return r;
        }
    }
    
    public String toHexString(){
        String r = "" + Long.toHexString(x) + " " + Long.toHexString(y);
        return r;
    }
    
    public static Point parsePoint(String hex){
        Point p = new Point();
        return p;
    }
}
