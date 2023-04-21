    public static String loadTextFromFile(File file, String charSet) throws UnsupportedEncodingException, FileNotFoundException {
        StringWriter writer = new StringWriter();
        Reader reader = null;
        try {
            reader = charSet == null ? new FileReader(file) : new InputStreamReader(new FileInputStream(file), charSet);
            StreamUtil.copy(writer, reader);
        } finally {
            StreamUtil.close(reader);
        }
        return writer.toString();
    }
