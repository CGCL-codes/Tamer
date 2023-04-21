    public short[] translateColor(ICC_Transform t, short src[], short dst[]) {
        NativeImageFormat srcFmt = createImageFormat(t, src, 0, true);
        NativeImageFormat dstFmt = createImageFormat(t, dst, srcFmt.getNumCols(), false);
        t.translateColors(srcFmt, dstFmt);
        return (short[]) dstFmt.getChannelData();
    }
