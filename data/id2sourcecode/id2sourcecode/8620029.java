    public boolean addOutputValue(RGBBase.Channel ch, int index, double addValue) {
        checkSync();
        canUndo = false;
        undoChannel = ch;
        undoIndex = index;
        undoAddValue = addValue;
        if (ch == RGBBase.Channel.W) {
            double[] results = new double[3];
            results[0] = rgbOutput[0][index] + addValue;
            results[1] = rgbOutput[1][index] + addValue;
            results[2] = rgbOutput[2][index] + addValue;
            for (int x = 0; x < 3; x++) {
                if (results[x] > 255 || results[x] < 0) {
                    return false;
                }
            }
            for (int x = 0; x < 3; x++) {
                rgbOutput[x][index] = results[x];
                outputRGBArray[index].setValue(ch.getChannelByArrayIndex(x), results[x], RGB.MaxValue.Double255);
            }
        } else {
            double result = rgbOutput[ch.getArrayIndex()][index] + addValue;
            if (result > 255 || result < 0) {
                return false;
            }
            rgbOutput[ch.getArrayIndex()][index] = result;
            outputRGBArray[index].setValue(ch, result, RGB.MaxValue.Double255);
            canUndo = true;
        }
        return true;
    }
