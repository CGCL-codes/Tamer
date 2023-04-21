    public synchronized int process(Buffer in, Buffer out) {
        if (!checkInputBuffer(in)) {
            return BUFFER_PROCESSED_FAILED;
        }
        if (isEOM(in)) {
            propagateEOM(out);
            return BUFFER_PROCESSED_OK;
        }
        int inOffset = in.getOffset();
        int inLen = in.getLength();
        double inRate = ((AudioFormat) inputFormat).getSampleRate();
        double outRate = ((AudioFormat) outputFormat).getSampleRate();
        int chnl = ((AudioFormat) inputFormat).getChannels();
        int bsize = ((AudioFormat) inputFormat).getSampleSizeInBits() / 8;
        int step = 0;
        if (chnl == 2) {
            if (bsize == 2) step = 4; else step = 2;
        } else {
            if (bsize == 2) step = 2; else step = 1;
        }
        if (outRate == 0.0 || inRate == 0.0) {
            return BUFFER_PROCESSED_FAILED;
        }
        double ratio = inRate / outRate;
        int outLen = (int) ((inLen - inOffset) * outRate / inRate + 0.5);
        switch(step) {
            case 2:
                if (outLen % 2 == 1) outLen++;
                break;
            case 4:
                if (outLen % 4 != 0) outLen = (outLen / 4 + 1) << 2;
                break;
        }
        if (inputFormat.getDataType() == Format.byteArray) {
            return doByteCvrt(in, inLen, inOffset, out, outLen, step, ratio);
        } else if (inputFormat.getDataType() == Format.shortArray) {
            return doShortCvrt(in, inLen, inOffset, out, outLen, step, ratio);
        } else if (inputFormat.getDataType() == Format.intArray) {
            return doIntCvrt(in, inLen, inOffset, out, outLen, step, ratio);
        }
        return BUFFER_PROCESSED_FAILED;
    }
