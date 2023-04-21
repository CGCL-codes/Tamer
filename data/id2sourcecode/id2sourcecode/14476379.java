    protected void initInterpolationVarious() {
        List<Patch>[] patchListArray = filterPatchListArray(RGB.Channel.RGBWChannel);
        int channels = patchListArray.length;
        int[] sizeArray = new int[channels];
        double[][] codeArray = new double[channels][];
        double[][][] rgbwArray = new double[channels][][];
        for (int x = 0; x < channels; x++) {
            List<Patch> patchList = patchListArray[x];
            int size = patchList.size();
            sizeArray[x] = size;
            codeArray[x] = new double[size];
            rgbwArray[x] = new double[size][];
            int index = 0;
            for (Patch p : patchList) {
                codeArray[x][index] = p.getRGB().getValue(RGBBase.Channel.getChannelByArrayIndex(x));
                rgbwArray[x][index] = getDomainValues(p.getXYZ());
                index++;
            }
        }
        rArray = DoubleArray.transpose(rgbwArray[0]);
        gArray = DoubleArray.transpose(rgbwArray[1]);
        bArray = DoubleArray.transpose(rgbwArray[2]);
        wArray = DoubleArray.transpose(rgbwArray[3]);
        rInterpolator = new Interpolator(codeArray[0], rArray);
        gInterpolator = new Interpolator(codeArray[1], gArray);
        bInterpolator = new Interpolator(codeArray[2], bArray);
        wInterpolator = new Interpolator(codeArray[3], wArray);
    }
