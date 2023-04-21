    private void readMP3Properties() {
        filebean.setBitrate(exttags.getBitrateI());
        filebean.setLength(exttags.getRuntime());
        filebean.setMode((byte) exttags.getChannelMode());
        filebean.setSamplerate(exttags.getFrequencyI());
        String codec = "MPEG " + exttags.getMpegIDS() + " Layer " + exttags.getLayerS();
        filebean.setCodec(codec);
        if (filebean.getLength() != 0) {
            int bav = filebean.getFilesize() * 8 / filebean.getLength() / 1024;
            filebean.setBitrateAverage(bav);
        }
    }
