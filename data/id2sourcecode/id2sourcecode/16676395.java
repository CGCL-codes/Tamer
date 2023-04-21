    public Object get(int pos) {
        if (pos > size()) return null; else {
            try {
                IntBuffer index = getIndex();
                if (index != null) {
                    int filePosition = index.get(pos * 2);
                    int objectSize = index.get(pos * 2 + 1);
                    FileChannel roChannel = new RandomAccessFile(new File(filename), "rw").getChannel();
                    ByteBuffer buf = roChannel.map(FileChannel.MapMode.PRIVATE, filePosition, objectSize);
                    byte[] byteBuf = new byte[objectSize];
                    buf.get(byteBuf, 0, byteBuf.length);
                    ObjectInputStream objStream = new ObjectInputStream(new ByteArrayInputStream(byteBuf));
                    Object object = objStream.readObject();
                    roChannel.close();
                    return object;
                }
            } catch (Exception ex) {
                Tools.logException(FileList.class, ex);
                ex.printStackTrace();
            }
        }
        return null;
    }
