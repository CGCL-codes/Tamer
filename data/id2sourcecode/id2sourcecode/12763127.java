    public byte[] download1(String strURL) throws IOException {
        URL url = new URL(strURL);
        URLConnection connection = url.openConnection();
        DataInputStream in = new DataInputStream(connection.getInputStream());
        int size = connection.getContentLength();
        System.out.println(size);
        byte[] bStream = new byte[size];
        int bytes_read = 0;
        while (bytes_read < size) {
            bytes_read += in.read(bStream, bytes_read, size);
        }
        return bStream;
    }
