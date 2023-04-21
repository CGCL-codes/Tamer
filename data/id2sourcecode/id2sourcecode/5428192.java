    private Document readMarcBinary(File marcBinary) throws IOException {
        InputStream input = new FileInputStream(marcBinary);
        MarcReader reader = new MarcStreamReader(input);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        MarcWriter writer = new MarcXmlWriter(bout, "UTF-8");
        while (reader.hasNext()) {
            Record record = reader.next();
            writer.write(record);
        }
        writer.close();
        return DOM.parse(bout.toByteArray());
    }
