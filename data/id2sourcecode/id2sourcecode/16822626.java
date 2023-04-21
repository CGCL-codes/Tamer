    public void saveDA(File file) throws IOException {
        FileOutputStream fout = new FileOutputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        DataOutputStream dout = new DataOutputStream(bout);
        for (int k = 0; k < 2560; k++) {
        }
        NNJDataSource tempSource = this.getNNJDataSource();
        for (int det = 0; det < this.getDataLayout().getChannelCount(); det++) for (int frame = 0; frame < this.getTotalFrameCount(); frame++) {
            dout.writeShort(Short.reverseBytes((short) ((double) tempSource.readDataPoint(det, frame) / tempSource.getDataExtraBits())));
        }
        dout.close();
        bout.close();
        fout.close();
    }
