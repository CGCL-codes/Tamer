    @Override
    public void writeFile(String fileName, NNJDataSource source) throws IOException {
        File file = new File(fileName);
        FileOutputStream fout = new FileOutputStream(file);
        BufferedOutputStream bout = new BufferedOutputStream(fout);
        DataOutputStream dout = new DataOutputStream(bout);
        for (int frame = 0; frame < this.getTotalFrameCount(); frame++) {
            for (int det = 0; det < this.getDataLayout().getChannelCount(); det++) dout.writeShort(Short.reverseBytes((short) ((double) source.readDataPoint(det, frame) / (double) source.getDataExtraBits())));
        }
        dout.close();
        bout.close();
        fout.close();
    }
