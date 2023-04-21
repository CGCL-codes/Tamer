    static byte[] getURLContent(URL url) throws Exception {
        if (c_ResourceLoader == null) {
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int read;
            while ((read = input.read()) != -1) {
                output.write(read);
            }
            return output.toByteArray();
        } else {
            return c_ResourceLoader.getZipResource(url).getURLContent();
        }
    }
