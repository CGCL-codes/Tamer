    public static int arrToDNAStrRev(byte[] arr, int arrstart, int len, byte[] out, int outpos) {
        int dnalen = (len + 1) / 2;
        int arrpos = arrstart + len - 1;
        while (arrpos > arrstart) {
            out[outpos] = (byte) ((byteToDNA(arr[arrpos]) << 4) | (byteToDNA(arr[arrpos - 1])));
            outpos++;
            arrpos -= 2;
        }
        if (arrpos == arrstart) {
            out[outpos] = (byte) ((byteToDNA(arr[arrpos]) << 4) | (space));
        }
        return dnalen;
    }
