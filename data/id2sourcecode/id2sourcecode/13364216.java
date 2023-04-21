    String infalte(byte[] buffer) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(buffer);
        InflaterInputStream inflater = new InflaterInputStream(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] temparr = new byte[1024];
        int count = 0;
        while ((count = inflater.read(temparr)) > 0) output.write(temparr, 0, count);
        buffer = output.toByteArray();
        return new String(buffer, "GBK");
    }
