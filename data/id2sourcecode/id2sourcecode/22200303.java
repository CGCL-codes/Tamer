    public void addImageHistoryTag(File file) {
        File dst = null;
        IImageMetadata metadata = null;
        JpegImageMetadata jpegMetadata = null;
        TiffImageMetadata exif = null;
        OutputStream os = null;
        TiffOutputSet outputSet = new TiffOutputSet();
        try {
            metadata = Sanselan.getMetadata(file);
        } catch (ImageReadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (metadata != null) {
            jpegMetadata = (JpegImageMetadata) metadata;
        }
        if (jpegMetadata != null) {
            exif = jpegMetadata.getExif();
        }
        if (exif != null) {
            try {
                outputSet = exif.getOutputSet();
            } catch (ImageWriteException e) {
                e.printStackTrace();
            }
        }
        if (outputSet != null) {
            TiffOutputField imageHistoryPre = outputSet.findField(TiffConstants.EXIF_TAG_IMAGE_HISTORY);
            if (imageHistoryPre != null) {
                outputSet.removeField(TiffConstants.EXIF_TAG_IMAGE_HISTORY);
            }
            try {
                String fieldData = "ImageHistory-" + System.currentTimeMillis();
                TiffOutputField imageHistory = new TiffOutputField(ExifTagConstants.EXIF_TAG_IMAGE_HISTORY, TiffFieldTypeConstants.FIELD_TYPE_ASCII, fieldData.length(), fieldData.getBytes());
                TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
                exifDirectory.add(imageHistory);
            } catch (ImageWriteException e) {
                e.printStackTrace();
            }
        }
        try {
            dst = File.createTempFile("temp-" + System.currentTimeMillis(), ".jpeg");
            os = new FileOutputStream(dst);
            os = new BufferedOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new ExifRewriter().updateExifMetadataLossless(file, os, outputSet);
        } catch (ImageReadException e) {
            e.printStackTrace();
        } catch (ImageWriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
        try {
            FileUtils.copyFile(dst, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
