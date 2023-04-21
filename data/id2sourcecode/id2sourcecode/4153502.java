    public void testUploadByteArray() throws IOException, FlickrException, SAXException {
        File imageFile = new File(properties.getProperty("imagefile"));
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(imageFile);
            out = new ByteArrayOutputStream();
            int b = -1;
            while ((b = in.read()) != -1) {
                out.write((byte) b);
            }
            UploadMetaData metaData = new UploadMetaData();
            metaData.setTitle("óöä");
            String photoId = uploader.upload(out.toByteArray(), metaData);
            assertNotNull(photoId);
            pint.delete(photoId);
        } finally {
            IOUtilities.close(in);
            IOUtilities.close(out);
        }
    }
