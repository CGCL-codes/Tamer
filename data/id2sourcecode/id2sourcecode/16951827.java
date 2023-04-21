    public static void write(String url, String charset, SerializableStreamWriter writer) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(url), charset));
            String line = null;
            while ((line = reader.readLine()) != null) writer.write(line);
        } catch (FileNotFoundException e) {
            throw new IOException(e.getMessage());
        } catch (SerializerException e) {
            throw new IOException(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (SerializerException e) {
                    throw new IOException(e.getMessage());
                }
            }
            if (reader != null) reader.close();
        }
    }
