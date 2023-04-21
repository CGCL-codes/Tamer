    public void read(Buffer buffer) throws IOException {
        synchronized (this) {
            Object outdata = buffer.getData();
            if (outdata == null || !(outdata.getClass() == Format.intArray) || ((int[]) outdata).length < maxDataLength) {
                outdata = new int[maxDataLength];
                buffer.setData(outdata);
            }
            buffer.setFormat(rgbFormat);
            buffer.setTimeStamp((long) (seqNo * (1000 / frameRate) * 1000000));
            BufferedImage bi = robot.createScreenCapture(new Rectangle(x, y, width, height));
            bi.getRGB(0, 0, width, height, (int[]) outdata, 0, width);
            buffer.setSequenceNumber(seqNo);
            buffer.setLength(maxDataLength);
            buffer.setFlags(Buffer.FLAG_KEY_FRAME);
            buffer.setHeader(null);
            seqNo++;
        }
    }
