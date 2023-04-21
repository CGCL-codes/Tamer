    private void openDA(File file, int detToRead) throws IOException {
        FileInputStream fin = new FileInputStream(file);
        byte[] byteBuff;
        byteBuff = new byte[2560 * 2];
        fin.read(byteBuff);
        dataHeadDA = byteArrayToIntArray16Rev(byteBuff);
        this.frameCount = dataHeadDA[5 - 1];
        if (frameCount < 0) {
            frameCount = -frameCount * 1024;
        }
        this.dataWin = new NNJDataWindow(this);
        this.dataMask = new NNJDataMask();
        byteBuff = new byte[frameCount * 2];
        if (detToRead == -1) {
            this.data = new int[DATA_LAYOUT.getChannelCount()][];
            for (int det = 0; det < data.length; det++) {
                fin.read(byteBuff);
                this.data[det] = byteArrayToIntArray16Rev(byteBuff);
                this.data[det] = K.multiply(this.data[det], DATA_EXTRA_BITS);
            }
            this.dataLoaded = true;
        } else if (detToRead == -2) {
            this.data = new int[DATA_LAYOUT.getChannelCount()][];
            for (int det = 0; det < data.length; det++) {
                fin.read(byteBuff);
                this.data[det] = byteArrayToIntArray16Rev(byteBuff);
                this.data[det] = K.multiply(this.data[det], DATA_EXTRA_BITS);
            }
        } else if (DATA_LAYOUT.isValidDetector(detToRead)) {
            fin.skip((long) frameCount * (long) detToRead);
            fin.read(byteBuff);
            this.data[detToRead] = byteArrayToIntArray16Rev(byteBuff);
            this.data[detToRead] = K.multiply(this.data[detToRead], DATA_EXTRA_BITS);
        }
        fin.close();
    }
