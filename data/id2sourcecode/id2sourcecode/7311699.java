    public float getBin(long bin) {
        float f = -1.0f;
        try {
            if (in == null) {
                in = new FileInputStream(this.dataFile);
                ByteBuffer bb = in.getChannel().map(MapMode.READ_ONLY, bin * 4, this.bufferSize * 4);
                bb.order(ByteOrder.nativeOrder());
                fb = bb.asFloatBuffer();
                currentFilePos = bin;
            }
            long lim = 0L;
            if (bin >= currentFilePos + this.bufferSize) {
                if ((bin + this.bufferSize) * 4 > fileLength) {
                    lim = fileLength - (this.bufferSize * 4);
                } else {
                    lim = bin * 4;
                }
                ByteBuffer bb = in.getChannel().map(MapMode.READ_ONLY, lim, this.bufferSize * 4);
                bb.order(ByteOrder.nativeOrder());
                fb = bb.asFloatBuffer();
                currentFilePos = lim / 4;
            }
            if (bin > currentFilePos + fb.position()) {
                while (bin != currentFilePos + fb.position() - 1) {
                    fb.get();
                }
            }
            if (bin < currentFilePos + fb.position()) {
                in.close();
                in = new FileInputStream(this.dataFile);
                ByteBuffer bb = in.getChannel().map(MapMode.READ_ONLY, bin * 4, this.bufferSize * 4);
                bb.order(ByteOrder.nativeOrder());
                fb = bb.asFloatBuffer();
                currentFilePos = bin;
            }
            if (bin == currentFilePos + fb.position()) {
                f = fb.get();
            } else {
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return f;
    }
