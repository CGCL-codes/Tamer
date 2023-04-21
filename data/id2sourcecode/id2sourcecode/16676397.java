    public Object set(int pos, Object element) {
        try {
            File file = new File(filename);
            int filePos = 0;
            if (file.exists()) filePos = (int) file.length();
            WritableByteChannel channel = new FileOutputStream(file, true).getChannel();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(element);
            int size = byteStream.size();
            ByteBuffer buf = ByteBuffer.allocateDirect(size);
            buf.put(byteStream.toByteArray());
            buf.flip();
            int numWritten = channel.write(buf);
            channel.close();
            buf = null;
            file = new File(indexname);
            channel = new FileOutputStream(file, true).getChannel();
            buf = ByteBuffer.allocateDirect(4 * 2);
            buf.putInt(filePos);
            buf.putInt(size);
            buf.flip();
            numWritten = channel.write(buf);
            channel.close();
            mIndex = null;
        } catch (Exception e) {
        }
        return element;
    }
