    @Override
    public void write(Writer writer, int depth) throws IOException {
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(StreamUtils.inputStreamToReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                StringUtils.encodeHTMLChars(line, writer);
                writer.write("\n");
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            writer.write("couldn't include the href: " + e);
        }
    }
