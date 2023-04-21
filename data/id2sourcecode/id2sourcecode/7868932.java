    protected static String getExternalSourceCode(Node n) throws MalformedURLException, IOException {
        String src = XMLHelper.getAttributeAsString(n.getAttributes(), ETLJob.EXTERNAL_SOURCE, null);
        if (src == null) return null;
        StringBuffer sb = new StringBuffer(1024);
        URL url = new URL(src);
        InputStream stream = url.openConnection().getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);
        char[] chars = new char[512];
        int len;
        while ((len = reader.read(chars)) != -1) {
            sb.append(chars, 0, len);
        }
        streamReader.close();
        reader.close();
        return sb.toString();
    }
