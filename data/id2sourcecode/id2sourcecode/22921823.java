    public void getFile(Long length, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            InputStream in = connection.getInputStream();
            for (int i = 0; i < length; i++) {
                fos.write((byte) in.read());
            }
        } catch (IOException e) {
            failed(address.toString());
        }
    }
