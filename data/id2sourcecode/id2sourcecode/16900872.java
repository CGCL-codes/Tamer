    public String getContent() {
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringWriter writer = new StringWriter();
            char[] data = new char[1024];
            int length;
            int position = 0;
            while ((length = reader.read(data)) >= 0) writer.write(data, 0, length);
            reader.close();
            writer.close();
            result = writer.toString();
        } catch (FileNotFoundException ex) {
            log.error("Failed to read file " + filename, ex);
        } catch (IOException ex) {
            log.error("Failed to read file " + filename, ex);
        }
        return result;
    }
