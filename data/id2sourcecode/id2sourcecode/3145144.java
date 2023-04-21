    public static boolean runGrey(SegmentImageData sourceData, final byte[] dest) {
        if (sourceData == null || sourceData.data == null) {
            SWT.error(SWT.ERROR_CANNOT_BE_ZERO);
            return false;
        }
        if (!(sourceData.bpp == 3 || sourceData.bpp == 4)) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
            return false;
        }
        int scanLineEnd = -1;
        final int step = sourceData.bpp;
        if (sourceData.pad != 0) {
            scanLineEnd = sourceData.start + sourceData.scanLineLength - step;
        }
        final int gs = sourceData.greenShift;
        final int bs = sourceData.blueShift;
        final int rs = sourceData.redShift;
        int r, g, b, gray;
        final byte[] source = sourceData.data;
        final int length = sourceData.length - sourceData.pad;
        for (int i = sourceData.start; i < length; i += step) {
            b = source[i + rs] & 0xff;
            g = source[i + gs] & 0xff;
            r = source[i + bs] & 0xff;
            gray = ((greenGrey[(b & 0xff)]) + (blueGrey[(g & 0xff)]) + (redGrey[(r & 0xff)]));
            gray = gray > 255 ? 255 : gray;
            dest[i] = dest[i + 1] = dest[i + 2] = (byte) gray;
            if (i == scanLineEnd) {
                scanLineEnd += sourceData.scanLineLength + sourceData.pad;
                i += sourceData.pad;
            }
        }
        return true;
    }
