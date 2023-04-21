    public static byte[] getBytesFromFile(File file) throws IOException {
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            byte[] buffer = new byte[4096];
            inputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            byte[] byteArray = null;
            byteArray = outputStream.toByteArray();
            return byteArray;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
