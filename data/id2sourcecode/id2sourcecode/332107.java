    public static void main(String[] args) {
        double a = 4;
        double d = 0.00000001;
        double low = 0;
        double high = a;
        double middle = (low + high) / 2;
        while (low + d < high) {
            middle = (low + high) / 2;
            if (middle * middle > a) high = middle; else if (middle * middle < a) low = middle; else break;
        }
        System.out.println(middle + " * " + middle + " = " + a);
    }
