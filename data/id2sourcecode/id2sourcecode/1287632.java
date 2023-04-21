    public String wtf(int inT) {
        int b = Integer.MAX_VALUE - 1;
        int min = (Math.min(inT, b));
        int rounds = (min + 1) / 2;
        return new String(inT + ";" + b + ";" + min + ";" + rounds + ";");
    }
