    private Garden loadFrom(InputStream is) throws IOException, InvalidFormatException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buffer = new byte[65536];
        while (is.available() > 0) {
            int read = is.read(buffer, 0, 65536);
            tmp.write(buffer, 0, read);
        }
        ByteArrayInputStream gardenStream = new ByteArrayInputStream(tmp.toByteArray());
        try {
            return v1.load(gardenStream);
        } catch (InvalidFormatException ex) {
        }
        gardenStream.reset();
        return xmlV1.load(gardenStream);
    }
