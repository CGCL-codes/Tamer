    public byte[] receive() {
        try {
            ByteArrayOutputStream m = new ByteArrayOutputStream();
            byte b;
            while ((b = (byte) in.read()) != -1) m.write(b);
            return m.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
