package ecc;

import java.util.ArrayList;
import com.google.common.math.LongMath;
import java.math.RoundingMode;


/**
 *
 * @author 
 */
public class Curve {
    //y^2 = x^3+ax+b
    private long a = 12;
    private long b = 6;
    private long p = 3;
    public ArrayList<Point> ellipticGroup;

    public long getA() {
        return a;
    }

    public long getP() {
        return p;
    }

    public void setP(long p) {
        this.p = p;
    }
    
    //Find all elliptic group of equation y^2 = x^3+ax+b
    public void setEllipticGroup(){
       ellipticGroup = new ArrayList<>();
       long y2, aCongruence, y, y3;
       long x = 0;
       
       while (x <= p-1){
            y2 = (x*x*x + a*x + b);
            aCongruence = LongMath.mod(y2, p);
            for (int j = 1; j<=(p-1); j++){
                y3 = p*j + aCongruence;
                if (isPerfectSquare(y3)) {
                    y = LongMath.sqrt(y3, RoundingMode.UP);
                    Point po = new Point(x,y);
                    if (!ellipticGroup.contains(po)){
                        ellipticGroup.add(po);
                    }
                    Point pp = new Point(x,p-y);
                    if (!ellipticGroup.contains(pp)) {
                        ellipticGroup.add(pp);
                    }
                    
                }
            }
            x++;
       }
    }
    
    //Returns y value from a point in curve with x given 
    public long getY(long x) {
        long y2, aCongruence, y = -1, y3;
        y2 = (x*x*x + a*x + b);
        aCongruence = LongMath.mod(y2, p);
        boolean found = false;
        int j = 0;
        while (!found && j<p-1){
            y3 = p*j + aCongruence;
            if (isPerfectSquare(y3)) {
                y = LongMath.sqrt(y3, RoundingMode.UP);
                found = true;
            }
            j++;
        }
        return y;
    }
    
    /**
     * Check if a long integer is a perfect square
     * @param n, number to be checked
     * @return true if n is perfect square
     */
    public final static boolean isPerfectSquare(long n){
        if (n < 0)
            return false;

        switch((int)(n & 0xF))
        {
        case 0: case 1: case 4: case 9:
            long tst = (long)Math.sqrt(n);
            return tst*tst == n;

        default:
            return false;
        }
    }    
}