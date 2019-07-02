public class Fraction {

    int denominator, numerator;
    public boolean simplified;

    public Fraction(int denominator, int numerator){
        this.denominator = denominator;
        this.numerator = numerator;
    }

    public Fraction(int num){
        this.numerator = num;
        this.denominator = 1;
    }

    public Fraction(double num){
        double temp = num;
        int order = 0;
        while (temp*10 - temp != 0)
            temp *= 10;
        this.numerator = (int)temp;
        this.denominator = (int)Math.pow(10, order);
    }

    public Fraction(double num,int repeatFrom,int repeatTo){

    }

    public void simplify(){
        int gcd = gcd(numerator, denominator);
        while (gcd != 1){
            numerator /= gcd;
            denominator /= gcd;
        }
        simplified = true;
    }

    private static int gcd(int p, int q){
        if(q == 0)
            return p;
        return gcd(q, p%q);
    }

    @Override
    public String toString(){
        String str = Integer.toString(numerator);
        if(denominator != 1){
            str += "/" + denominator;
        }
        return str;
    }

}
