    private void copyMember(File file, String tag, String outputEncoding, PrintWriter writer) throws IOException, MalformedURLException, UnsupportedEncodingException {
        ZipInputStream zis = openZipStreamAt(toURL(file), "." + tag);
        if (zis != null) {
            InputStreamReader isr = new InputStreamReader(zis, outputEncoding);
            BufferedReader reader = new BufferedReader(isr);
            String firstLine = reader.readLine();
            if (firstLine.startsWith("<?xml")) {
                reader.readLine();
            } else {
                writer.println(firstLine);
            }
            readerToWriter(reader, writer);
            zis.close();
            reader.close();
        }
    }
