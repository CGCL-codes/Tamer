    private static byte[] readTestFile() throws Exception {
        FileInputStream is = new FileInputStream(TestResources.ANIMAL_FILE);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = -1;
        while ((read = is.read(buffer)) >= 0) {
            if (read > 0) {
                bos.write(buffer, 0, read);
            }
        }
        is.close();
        return bos.toByteArray();
    }
