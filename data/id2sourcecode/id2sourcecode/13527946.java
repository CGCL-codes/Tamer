    protected byte[] getImageData(PDXObjectImage image) throws IOException {
        if (image instanceof PDJpeg) {
            List<String> DCT_FILTERS = new ArrayList<String>();
            DCT_FILTERS.add(COSName.DCT_DECODE.getName());
            DCT_FILTERS.add(COSName.DCT_DECODE_ABBREVIATION.getName());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            InputStream data = image.getPDStream().getPartiallyFilteredStream(DCT_FILTERS);
            byte[] buf = new byte[1024];
            int amountRead = -1;
            while ((amountRead = data.read(buf)) != -1) os.write(buf, 0, amountRead);
            os.close();
            return os.toByteArray();
        } else {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            image.write2OutputStream(os);
            os.close();
            return os.toByteArray();
        }
    }
