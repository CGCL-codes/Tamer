    public static int modelFileNumberFromFloat(float fDotM) {
        int file = (int) (fDotM);
        int model = (int) ((fDotM - file + 0.00001) * 10000);
        while (model % 10 == 0) model /= 10;
        return file * 1000000 + model;
    }
