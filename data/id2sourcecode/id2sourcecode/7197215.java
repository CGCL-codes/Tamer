    public static void main(String[] args) throws IOException {
        String test = null;
        java.io.Reader reader;
        if (args.length == 0) {
            test = "This is a test.  This is only a test.  If this had been an actual string, it" + " would have been proceeded by content that matters to the application.  BEEP!!!";
            java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
            java.io.Writer writer = new java.io.OutputStreamWriter(new ExportStream(bytes));
            writer.write(test);
            writer.close();
            reader = new java.io.InputStreamReader(new ImportStream(new java.io.ByteArrayInputStream(bytes.toByteArray())));
        } else reader = new java.io.InputStreamReader(new ImportStream(new java.io.ByteArrayInputStream(args[0].getBytes())));
        java.io.StringWriter str = new java.io.StringWriter();
        int read = reader.read();
        while (read >= 0) {
            str.write(read);
            read = reader.read();
        }
        if (test != null) {
            if (!test.equals(str.toString())) System.err.println("Import/Export does not work!  " + str.toString()); else System.out.println("Import/Export successful.");
        } else System.out.println(str.toString());
    }
