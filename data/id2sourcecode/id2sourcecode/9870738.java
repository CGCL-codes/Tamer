    protected byte[] read() throws Exception {
        if (new File(pathToDBFile()).exists()) {
            FileInputStream fileIn = new FileInputStream(pathToDBFile());
            ByteArrayOutputStream toReturn = new ByteArrayOutputStream();
            byte[] buffer = new byte[32 * 1024];
            int read = 0;
            while ((read = fileIn.read(buffer)) != -1) {
                toReturn.write(buffer, 0, read);
            }
            fileIn.close();
            return toReturn.toByteArray();
        } else {
            return new byte[0];
        }
    }
