    public MD5Key(Object[] objs) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        for (int i = 0; i < objs.length; ++i) objectOutputStream.writeObject(objs[i]);
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        digester.update(byteArrayOutputStream.toByteArray());
        checksum = digester.digest();
        objectOutputStream.close();
        byteArrayOutputStream.close();
    }
