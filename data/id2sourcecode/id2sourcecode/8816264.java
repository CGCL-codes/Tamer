    private void populateExifData() {
        if (this.exifDataPopulated) {
            return;
        }
        this.exifDataPopulated = true;
        File exifFile = new File(this.filename);
        FileInputStream exifInputStream;
        try {
            exifInputStream = new FileInputStream(exifFile);
        } catch (FileNotFoundException e) {
            this.setErrorMsg("file " + this.filename + " does not exist");
            return;
        }
        if (!exifFile.isFile()) {
            this.setErrorMsg("file " + this.filename + " is not a exif file");
            return;
        }
        ByteBuffer exifBuffer = this.getExifDataBuffer(exifInputStream.getChannel());
        if (exifBuffer == null) {
            return;
        }
        if (this.debugMode) {
            System.out.println("Have EXIF buffer of size " + (exifBuffer.limit() - exifBuffer.position()) + ", limit " + exifBuffer.limit());
        }
        this.parseExifBuffer(exifBuffer);
    }
