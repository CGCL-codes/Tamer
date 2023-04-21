    public String openFile(File file) {
        try {
            Reader reader = new FileReader(file);
            StringWriter writer = new StringWriter();
            int b;
            while ((b = reader.read()) >= 0) writer.write(b);
            setText(writer.toString());
            reader.close();
            return writer.toString();
        } catch (Exception ex) {
            return "";
        }
    }
