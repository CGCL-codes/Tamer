    public RandomZMatrixBuilder(ImagePlus imp, int noOfPixelsN, int noOfImagesP) {
        this.imp = imp;
        this.noOfPixelsN = noOfPixelsN;
        this.noOfChannels = imp.getChannelProcessor().getNChannels();
        this.noOfImagesP = noOfImagesP;
        this.imgHeight = imp.getHeight();
        this.imgWidth = imp.getWidth();
    }
