    public static boolean runGrey(SegmentImageData sourceData, final byte[] dest, int[] greenGrey, int[] blueGrey, int[] redGrey, int[] bclut, IProgressMonitor monitor) {
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
        scanLineEnd = sourceData.start + sourceData.scanLineLength - step;
        final int bs = sourceData.blueShift;
        final int gs = sourceData.greenShift;
        final int rs = sourceData.redShift;
        int r, g, b, gray;
        final byte[] source = sourceData.data;
        final int length = sourceData.length - sourceData.pad;
        for (int i = sourceData.start; i < length; i += step) {
            b = bclut[source[i + bs] & 0xff];
            g = bclut[source[i + gs] & 0xff];
            r = bclut[source[i + rs] & 0xff];
            gray = ((greenGrey[g]) + (blueGrey[b]) + (redGrey[r]));
            gray = gray > 255 ? 255 : gray;
            dest[i] = dest[i + 1] = dest[i + 2] = (byte) gray;
            if (i == scanLineEnd) {
                scanLineEnd += sourceData.scanLineLength + sourceData.pad;
                i += sourceData.pad;
                if (monitor != null) {
                    monitor.worked(1);
                }
            }
        }
        return true;
    }
