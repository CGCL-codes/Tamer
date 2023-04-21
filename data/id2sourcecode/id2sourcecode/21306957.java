    public static byte[] getData(InputStream iStrm, int length) throws IOException {
        byte[] imageData;
        if (length != -1) {
            imageData = new byte[length];
            DataInputStream din = new DataInputStream(iStrm);
            din.readFully(imageData);
        } else {
            ByteArrayOutputStream bStrm = new ByteArrayOutputStream();
            int len;
            for (byte[] buffer = new byte[Math.max(32, iStrm.available())]; (len = iStrm.read(buffer)) != -1; ) bStrm.write(buffer, 0, len);
            imageData = bStrm.toByteArray();
            bStrm.close();
        }
        return imageData;
    }
