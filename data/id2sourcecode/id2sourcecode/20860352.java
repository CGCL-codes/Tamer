    public int[] CalimeroFrame2BasysFrame(PointPDUXlator calimeroFrame) {
        byte[] cali = calimeroFrame.getAPDUByteArray();
        int[] basys = new int[cali.length - 1];
        basys[0] = cali[1] + 128;
        int j = 2;
        for (int i = 1; i < basys.length; i++) {
            basys[i] = cali[i + 1];
            j++;
        }
        return basys;
    }
