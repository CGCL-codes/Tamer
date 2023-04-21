    private SpeciesList loadFrom(InputStream is) throws IOException, InvalidFormatException {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buffer = new byte[65536];
        while (is.available() > 0) {
            int read = is.read(buffer, 0, 65536);
            tmp.write(buffer, 0, read);
        }
        ByteArrayInputStream speciesStream = new ByteArrayInputStream(tmp.toByteArray());
        try {
            return v1.load(speciesStream);
        } catch (InvalidFormatException ex) {
            throw ex;
        }
    }
