    public void fromPersistentFormat(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        name = dis.readUTF();
        infoURL = dis.readUTF();
        displayTime = dis.readLong();
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        byte[] ba = new byte[1 * 1024];
        int read;
        while ((read = dis.read(ba)) != -1) {
            temp.write(ba, 0, read);
        }
        data = temp.toByteArray();
    }
