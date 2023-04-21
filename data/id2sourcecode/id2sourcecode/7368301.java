    public PictureDescriptionInfo(PictureData pic, boolean autoFill, boolean overwrite) {
        super(getDescriptionFile(pic.getFile()));
        imageFile = pic.getFile();
        this.autoFill = autoFill;
        this.overwrite = overwrite;
        if (autoFill) {
            readExifData(overwrite);
            calcOriginalDim(pic);
        }
    }
