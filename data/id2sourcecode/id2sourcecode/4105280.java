    private void sendFile(File file, OutputStream out) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        byte[] buff = new byte[2048];
        int read = 0;
        int total = 0;
        while ((read = fileIn.read(buff)) != -1) {
            total += read;
            if (GlobalProps.DEBUG) {
                System.out.println("sendFile read: " + read + " total: " + total);
            }
            out.write(buff, 0, read);
        }
        if (GlobalProps.DEBUG) {
            System.out.println("sendFile read total: " + total);
        }
        fileIn.close();
        out.close();
    }
