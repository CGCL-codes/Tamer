    public void readFrame(Buffer buffer) {
        if (buffer == null) return;
        if (!isEnabled()) {
            buffer.setDiscard(true);
            return;
        }
        buffer.setFormat(outFormat);
        Object obj = buffer.getData();
        byte[] data;
        long location;
        int needDataSize;
        needDataSize = outFormat.getChannels() * outFormat.getSampleSizeInBits() * 4000;
        if ((obj == null) || (!(obj instanceof int[])) || (((byte[]) obj).length < needDataSize)) {
            data = new byte[needDataSize];
            buffer.setData(data);
        } else {
            data = (byte[]) obj;
        }
        if (parser.getNextAudioFrame(data, 0, needDataSize)) {
            int size = parser.getAudioSampleNumber();
            buffer.setOffset(0);
            buffer.setLength(size);
            double audioTime = parser.getAudioSampleTimestamp();
            long ts = (long) (audioTime * 1000000000);
            buffer.setTimeStamp(ts);
            long tmp = System.currentTimeMillis();
            lts = ts;
            lt = tmp;
        } else {
            buffer.setLength(0);
            buffer.setEOM(true);
        }
    }
