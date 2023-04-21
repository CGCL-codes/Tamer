    public static byte[] arrToDNA(byte[] arr, int start, int len) {
        int dnalen = (len + 1) / 2;
        byte[] dna = new byte[dnalen];
        arrToDNAStr(arr, start, len, dna, 0);
        return dna;
    }
