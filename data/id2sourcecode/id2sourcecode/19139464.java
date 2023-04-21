    public static byte[] loadBinaryFile(File file) throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        try {
            int aByte;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            while ((aByte = input.read()) != -1) bytes.write(aByte);
            return bytes.toByteArray();
        } finally {
            if (input != null) input.close();
        }
    }
