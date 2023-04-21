    public void run() {
        try {
            byte[] buffer = new byte[100];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }
