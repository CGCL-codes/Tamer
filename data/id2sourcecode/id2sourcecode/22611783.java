    public String URLtoString(URL url) throws IOException {
        String xml = null;
        if (url != null) {
            String protocol = url.getProtocol().toLowerCase();
            if (protocol.startsWith(Http.HTTP) || protocol.startsWith(Http.HTTPS)) {
                return URLtoString(url.toString());
            }
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            ByteArrayMaker bam = new ByteArrayMaker();
            byte[] bytes = new byte[512];
            for (int i = is.read(bytes, 0, 512); i != -1; i = is.read(bytes, 0, 512)) {
                bam.write(bytes, 0, i);
            }
            xml = new String(bam.toByteArray());
            is.close();
            bam.close();
        }
        return xml;
    }
