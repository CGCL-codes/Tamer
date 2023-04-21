    @Override
    public void convert(Reader reader, Writer writer, ConversionParameters params) throws ConverterException, IOException {
        try {
            char[] buffer = new char[1024];
            int num = 0;
            while ((num = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, num);
            }
            writer.flush();
        } finally {
            closeReader(reader);
            closeWriter(writer);
        }
    }
