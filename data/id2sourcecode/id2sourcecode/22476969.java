    public String convertTime(long millis) {
        String[] symbols = { "wks", "days", "hrs", "mins", "secs" };
        long[] multiples = { 7L, 24L, 60L, 60L };
        long[] units = new long[5];
        units[4] = 1000;
        for (int i = 3; i >= 0; i--) units[i] = units[i + 1] * multiples[i];
        StringBuilder b = new StringBuilder();
        long rest = millis;
        for (int i = 0; i < 5; i++) {
            int num = (int) (rest / units[i]);
            if (num > 0) b.append(num + symbols[i] + " ");
            rest %= units[i];
        }
        return b.toString();
    }
