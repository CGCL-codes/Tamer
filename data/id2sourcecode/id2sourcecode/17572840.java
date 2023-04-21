    public static void setDate(String path, Date date) throws ImageReadException, IOException, ClassCastException, ImageWriteException {
        File source = new File(path);
        File temp = null;
        OutputStream os = null;
        String stringDate = formatter.format(date);
        TiffOutputSet propiertiesSet = new TiffOutputSet();
        TiffOutputDirectory exifDirectory;
        propiertiesSet = ((JpegImageMetadata) Sanselan.getMetadata(source)).getExif().getOutputSet();
        propiertiesSet.removeField(TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
        TiffOutputField newfieldDate = new TiffOutputField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, TiffFieldTypeConstants.FIELD_TYPE_ASCII, stringDate.length(), stringDate.getBytes());
        exifDirectory = propiertiesSet.getOrCreateExifDirectory();
        exifDirectory.add(newfieldDate);
        temp = File.createTempFile("temp-" + System.currentTimeMillis(), ".jpg");
        os = new FileOutputStream(temp);
        os = new BufferedOutputStream(os);
        ExifRewriter rewrite = new ExifRewriter();
        rewrite.updateExifMetadataLossless(source, os, propiertiesSet);
        os.close();
        FileUtils.copyFile(temp, source);
    }
