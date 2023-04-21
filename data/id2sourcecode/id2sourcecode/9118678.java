    public static int arrToDNAStr(byte[] arr, int arrpos, int len, byte[] out, int outpos) {
        int dnalen = (len + 1) / 2;
        int arrend = arrpos + len;
        while (arrpos + 1 < arrend) {
            out[outpos] = (byte) ((byteToDNA(arr[arrpos]) << 4) | (byteToDNA(arr[arrpos + 1])));
            outpos++;
            arrpos += 2;
        }
        if (arrpos < arrend) {
            out[outpos] = (byte) ((byteToDNA(arr[arrpos]) << 4) | (space));
        }
        return dnalen;
    }
