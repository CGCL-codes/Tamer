    public void translateColor(ICC_Transform t, BufferedImage src, BufferedImage dst) {
        NativeImageFormat srcIF = NativeImageFormat.createNativeImageFormat(src);
        NativeImageFormat dstIF = NativeImageFormat.createNativeImageFormat(dst);
        if (srcIF != null && dstIF != null) {
            t.translateColors(srcIF, dstIF);
            return;
        }
        srcIF = createImageFormat(src);
        dstIF = createImageFormat(dst);
        short srcChanData[] = (short[]) srcIF.getChannelData();
        short dstChanData[] = (short[]) dstIF.getChannelData();
        ColorModel srcCM = src.getColorModel();
        int nColorChannels = srcCM.getNumColorComponents();
        scaler.loadScalingData(srcCM.getColorSpace());
        ColorModel dstCM = dst.getColorModel();
        float alpha[] = null;
        boolean saveAlpha = srcCM.hasAlpha() && dstCM.hasAlpha();
        if (saveAlpha) {
            alpha = new float[src.getWidth() * src.getHeight()];
        }
        WritableRaster wr = src.getRaster();
        int srcDataPos = 0, alphaPos = 0;
        float normalizedVal[];
        for (int row = 0, nRows = srcIF.getNumRows(); row < nRows; row++) {
            for (int col = 0, nCols = srcIF.getNumCols(); col < nCols; col++) {
                normalizedVal = srcCM.getNormalizedComponents(wr.getDataElements(col, row, null), null, 0);
                if (saveAlpha) {
                    alpha[alphaPos++] = normalizedVal[nColorChannels];
                }
                scaler.scale(normalizedVal, srcChanData, srcDataPos);
                srcDataPos += nColorChannels;
            }
        }
        t.translateColors(srcIF, dstIF);
        nColorChannels = dstCM.getNumColorComponents();
        boolean fillAlpha = dstCM.hasAlpha();
        scaler.loadScalingData(dstCM.getColorSpace());
        float dstPixel[] = new float[dstCM.getNumComponents()];
        int dstDataPos = 0;
        alphaPos = 0;
        wr = dst.getRaster();
        for (int row = 0, nRows = dstIF.getNumRows(); row < nRows; row++) {
            for (int col = 0, nCols = dstIF.getNumCols(); col < nCols; col++) {
                scaler.unscale(dstPixel, dstChanData, dstDataPos);
                dstDataPos += nColorChannels;
                if (fillAlpha) {
                    if (saveAlpha) {
                        dstPixel[nColorChannels] = alpha[alphaPos++];
                    } else {
                        dstPixel[nColorChannels] = 1f;
                    }
                }
                wr.setDataElements(col, row, dstCM.getDataElements(dstPixel, 0, null));
            }
        }
    }
