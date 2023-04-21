    public PGBasic(File file) {
        this();
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int size = -1;
            while ((size = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            inputStream.close();
            out.close();
            this.pgValue = out.toByteArray();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
