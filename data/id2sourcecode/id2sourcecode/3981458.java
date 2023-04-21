    public void test() throws IOException, ConverterException {
        SwdPndEnrichedConverter converter = new SwdPndEnrichedConverter();
        FileInputStream inputStream = null;
        ByteArrayOutputStream out;
        try {
            inputStream = new FileInputStream("test/input/04126911X.out");
            out = new ByteArrayOutputStream();
            ConversionParameters params = new ConversionParameters();
            params.setSourceCharset("UTF-8");
            params.setTargetCharset("UTF-8");
            converter.convert(inputStream, out, params);
            System.out.println(new String(out.toByteArray(), "UTF-8"));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
