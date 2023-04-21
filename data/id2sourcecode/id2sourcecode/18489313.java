    public void addToODF(ZipOutputStream zo) throws IOException, ZipException, BarcodeException {
        ZipEntry ze = new ZipEntry("Pictures/" + getImageName());
        zo.putNextEntry(ze);
        if (isBarcode()) {
            Barcoder barcode = Barcoder.buildBarcoder(barcodeType);
            barcode.setValue(barcodeValue);
            barcode.setImageWidth(width);
            barcode.setImageHeight(height);
            barcode.setIncludeCaption(includeCaption);
            barcode.setCheckDigit(checkDigit);
            BarcoderImage bcimage = new BarcoderImage(barcode);
            bcimage.sendGifTo(zo);
        } else {
            FileInputStream fis = new FileInputStream(file);
            addFile(fis, zo);
        }
    }
