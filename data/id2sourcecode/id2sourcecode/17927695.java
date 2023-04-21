    public static void copyFile(InputStream source, String target) throws Exception {
        InputStreamReader reader = new InputStreamReader(source);
        FileWriter writer = new FileWriter(target);
        try {
            int c = -1;
            while ((c = reader.read()) != -1) writer.write(c);
        } catch (IOException e) {
            throw e;
        } finally {
            reader.close();
            writer.close();
        }
    }
