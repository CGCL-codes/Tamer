    public static void write(File file, Reader reader, boolean append) throws IOException {
        mkdirs(file);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, append));
            int data = -1;
            while ((data = reader.read()) != -1) {
                writer.write(data);
            }
        } finally {
            close(reader, file);
            close(writer, file);
        }
    }
