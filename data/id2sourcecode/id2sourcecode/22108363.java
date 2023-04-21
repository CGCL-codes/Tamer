    public String trainAsString(String model, String dataType, InputStream is) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int num;
        train(model, dataType, is.available());
        while ((num = is.read(buffer)) > 0) write(buffer, 0, num);
        int retSize = readInt();
        if (retSize > 0) {
            buffer = new byte[retSize];
            read(buffer);
            return new String(buffer);
        }
        return null;
    }
