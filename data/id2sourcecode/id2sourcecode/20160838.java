    private static void tidy(Reader reader, Writer writer) {
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setPrintBodyOnly(true);
        tidy.setForceOutput(false);
        tidy.parse(reader, writer);
    }
