    private void copyXmi(File file, String encoding, PrintWriter writer) throws IOException, MalformedURLException, UnsupportedEncodingException {
        ZipInputStream zis = openZipStreamAt(toURL(file), ".xmi");
        BufferedReader reader = new BufferedReader(new InputStreamReader(zis, encoding));
        reader.readLine();
        readerToWriter(reader, writer);
        zis.close();
        reader.close();
    }
