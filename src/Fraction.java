public class Fraction {

    int denominator, numerator;

    private static final double EPSILON = 1e-13;

    public Fraction(int numerator, int denominator){
        this.denominator = denominator;
        this.numerator = numerator;
        this.simplify();
    }

    public Fraction(int num){
        this.numerator = num;
        this.denominator = 1;
        this.simplify();
    }

    public Fraction(double num){
        double temp = num;
        int order = 0;
        while (Math.abs(temp - (int)temp ) >= EPSILON) {
            temp *= 10;
            order++;
        }
        this.numerator = (int)temp;
        this.denominator = (int)Math.pow(10, order);
        this.simplify();
    }

    public Fraction(String str){
        String[] num = str.split("/");
        if(num.length != 2)
            System.exit(-1);
        this.numerator = Integer.valueOf(num[0]);
        this.denominator = Integer.valueOf(num[1]);
        this.simplify();
    }

    public void simplify(){

        int gcd = gcd(numerator, denominator);
        while (gcd != 1 && gcd != 0){
            numerator /= gcd;
            denominator /= gcd;
            gcd = gcd(numerator, denominator);
        }
        if(denominator < 0){
            denominator = -denominator;
            numerator = -numerator;
        }
    }

    public boolean isSimplified(){
        return gcd(numerator, denominator) == 1;
    }

    private static int gcd(int p, int q){
        if(q == 0)
            return p;
        return gcd(q, p%q);
    }

    public static Fraction add(Fraction a, Fraction b){
        Fraction temp = new Fraction(a.numerator*b.denominator + b.numerator*a.denominator,a.denominator*b.denominator);
        temp.simplify();
        return temp;
    }

    public static Fraction subtract(Fraction a, Fraction b){
        Fraction temp = new Fraction(a.numerator*b.denominator - b.numerator*a.denominator,a.denominator*b.denominator);
        temp.simplify();
        return temp;
    }

    public static Fraction multiply(Fraction a, Fraction b){
        Fraction temp = new Fraction(a.numerator*b.numerator,a.denominator*b.denominator);
        temp.simplify();
        return temp;
    }

    public static Fraction divide(Fraction a, Fraction b){
        Fraction temp = new Fraction(a.numerator*b.denominator,a.denominator*b.numerator);
        temp.simplify();
        return temp;
    }

    public boolean equals(Fraction other){
        return this.numerator == other.numerator && this.denominator == other.denominator;
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
