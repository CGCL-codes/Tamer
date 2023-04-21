    public int minimumCommonDenominator(int number) {
        int denominator = (new IntegerNumber(number)).absoluteValue();
        int residue = absoluteValue();
        while (residue != 0) {
            int dividend = denominator;
            denominator = residue;
            residue = dividend % denominator;
        }
        return denominator;
    }
