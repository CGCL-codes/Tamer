    public String readRequest() {
        try {
            ByteArrayOutputStream m = new ByteArrayOutputStream();
            byte b;
            while ((b = (byte) in.read()) != -1) m.write(b);
            return m.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
