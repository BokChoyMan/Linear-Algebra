public class Fraction {

    //integer denominator and numerator
    int denominator, numerator;

    //accepted error with floating point subtraction
    private static final double EPSILON = 1e-13;

    /**
     * constructs a Fraction and simplify it
     * @param numerator
     * @param denominator
     */
    public Fraction(int numerator, int denominator){
        this.denominator = denominator;
        this.numerator = numerator;
        this.simplify();
    }

    /**
     * constructs a Fraction from an integer. The denominator is set to 1, including if numerator is 0.
     * @param num
     */
    public Fraction(int num){
        this.numerator = num;
        this.denominator = 1;
        this.simplify();
    }

    /**
     * constructs a Fraction from a double.
     * @param num
     */
    public Fraction(double num){
        double temp = num;
        //first finds the order of magnitude of the decimal number
        int order = 0;
        while (Math.abs(temp - (int)temp ) >= EPSILON) {
            temp *= 10;
            order++;
        }
        //constructs the Fraction by setting the numerator to the decimal part, and denominator to the order of magnitude
        //of the floating point number
        this.numerator = (int)temp;
        this.denominator = (int)Math.pow(10, order);
        this.simplify();
    }

    /**
     * constructs a Fraction from a String in the format in either "a/b", or a decimal, or an integer
     * @param str
     */
    public Fraction(String str){
        //split the String at the '/' sign
        String[] num = str.split("/");
        //if the String is split
        if(num.length == 2) {
            this.numerator = Integer.valueOf(num[0]);
            this.denominator = Integer.valueOf(num[1]);
        //if the String is not split
        }else if(num.length == 1){
            Fraction temp = new Fraction(Double.valueOf(num[0]));
            this.numerator = temp.numerator;
            this.denominator = temp.denominator;
        }else
            //exception to be implemented
            System.exit(-1);
        this.simplify();
    }

    /**
     * simplify the fraction by keeping finding the gcd
     */
    public void simplify(){

        int gcd = gcd(numerator, denominator);
        //if gcd is 1 or 0, the the fraction cannot be further simplified
        while (gcd != 1 && gcd != 0){
            numerator /= gcd;
            denominator /= gcd;
            gcd = gcd(numerator, denominator);
        }
        //if a negative number is in the denominator, the negative sign is brought to the top
        if(denominator < 0){
            denominator = -denominator;
            numerator = -numerator;
        }
    }


    /**
     * returns if the fraction is simplified.
     * @return
     */
    public boolean isSimplified(){
        return gcd(numerator, denominator) == 1 || gcd(numerator, denominator) == 0;
    }

    /**
     * recursively finds the greatest common denominator using euler's algorithm
     * @param p
     * @param q
     * @return
     */
    private static int gcd(int p, int q){
        if(q == 0)
            return p;
        return gcd(q, p%q);
    }

    /**
     * adds 2 Fractions by multiplying each Fraction's numerator by the denominator of the other Fraction.
     * The numerator is added, then the Fraction is simplified.
     * @param f
     * @return
     */
    public Fraction add(Fraction f){
        Fraction temp = new Fraction(this.numerator*f.denominator + f.numerator*this.denominator,this.denominator*f.denominator);
        temp.simplify();
        return temp;
    }

    /**
     *
     * @param f
     * @return
     */
    public Fraction subtract(Fraction f){
        Fraction temp = new Fraction(this.numerator*f.denominator - f.numerator*this.denominator,this.denominator*f.denominator);
        temp.simplify();
        return temp;
    }

    /**
     * multiply both Fraction's numerator and denominator, then the Fraction is simplified
     * @param f
     * @return
     */
    public Fraction multiply(Fraction f){
        Fraction temp = new Fraction(this.numerator*f.numerator,this.denominator*f.denominator);
        temp.simplify();
        return temp;
    }

    /**
     * 2 Fractions are multiplied by one's inverse
     * @param f
     * @return
     */
    public Fraction divide(Fraction f){
        Fraction temp = new Fraction(this.numerator*f.denominator,this.denominator*f.numerator);
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
