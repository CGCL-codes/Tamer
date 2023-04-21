    public void setViewBox(final float[][] newViewBox) {
        if (newViewBox != null) {
            if (newViewBox.length != 3 || newViewBox[0] == null || newViewBox[1] == null || newViewBox[2] == null || newViewBox[0].length != 2 || newViewBox[1][0] < 0 || newViewBox[2][0] < 0) {
                throw new IllegalArgumentException();
            }
        }
        modifyingNode();
        if (viewBox == null) {
            viewBox = new float[3][];
            viewBox[0] = new float[2];
            viewBox[1] = new float[1];
            viewBox[2] = new float[1];
        }
        viewBox[0][0] = newViewBox[0][0];
        viewBox[0][1] = newViewBox[0][1];
        viewBox[1][0] = newViewBox[1][0];
        viewBox[2][0] = newViewBox[2][0];
        recomputeTransformState();
        recomputeProxyTransformState();
        computeCanRenderEmptyViewBoxBit(viewBox);
        modifiedNode();
    }
