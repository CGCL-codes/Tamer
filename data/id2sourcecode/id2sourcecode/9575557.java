    public void writeGeoPosition(GeoPosition pos) throws ImageReadException, IOException, ImageWriteException {
        JpegImageMetadata jpegMetadata = getMetadata();
        TiffOutputSet os = getMetadata().getExif().getOutputSet();
        double lng = pos.getLongitude();
        double lat = pos.getLatitude();
        os.setGPSInDegrees(lng, lat);
        File dst = File.createTempFile("temp-" + System.currentTimeMillis(), ".jpeg");
        OutputStream out = new FileOutputStream(dst);
        out = new BufferedOutputStream(out);
        new ExifRewriter().updateExifMetadataLossless(image, out, os);
        out.close();
        FileUtils.copyFile(dst, image);
    }
