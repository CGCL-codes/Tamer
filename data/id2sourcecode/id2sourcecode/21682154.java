    public void addPosterization(RasterFilterListManager filterManager, IRasterRendering rendering) throws FilterTypeException {
        EnhancementStretchListManager elm = new EnhancementStretchListManager(filterManager);
        LinearStretchParams leParams = new LinearStretchParams();
        double min = data.getMin();
        double max = data.getMax();
        double[] stretchs = data.getStretchs();
        double distance = max - min;
        for (int i = 0; i < stretchs.length; i++) stretchs[i] = min + stretchs[i] * distance;
        double[] in = new double[(stretchs.length - 1) * 2 + 4];
        int[] out = new int[(stretchs.length - 1) * 2 + 4];
        in[0] = in[1] = min;
        out[0] = out[1] = 0;
        in[in.length - 1] = in[in.length - 2] = max;
        out[out.length - 1] = out[out.length - 2] = 255;
        boolean even = true;
        out[2] = 0;
        for (int i = 3; i < in.length - 2; i = i + 2) {
            if (even) out[i] = out[i + 1] = 255; else out[i] = out[i + 1] = 0;
            even = !even;
        }
        out[out.length - 2] = 255;
        for (int i = 2; i < in.length - 2; i = i + 2) in[i] = in[i + 1] = stretchs[(int) (i / 2)];
        leParams.rgb = true;
        leParams.red.stretchIn = in;
        leParams.red.stretchOut = out;
        leParams.green.stretchIn = in;
        leParams.green.stretchOut = out;
        leParams.blue.stretchIn = in;
        leParams.blue.stretchOut = out;
        elm.addEnhancedStretchFilter(leParams, lyr.getDataSource().getStatistics(), rendering.getRenderBands(), false);
    }
