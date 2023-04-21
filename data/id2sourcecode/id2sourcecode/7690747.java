    public static void copyFromResource(String resource, File file) throws IOException {
        if (!file.isFile()) {
            InputStream input = MainFrame.class.getResourceAsStream(resource);
            FileOutputStream output = new FileOutputStream(file);
            byte[] data = new byte[128];
            int bytesRead;
            while ((bytesRead = input.read(data)) != -1) output.write(data, 0, bytesRead);
            input.close();
            output.close();
        }
    }
