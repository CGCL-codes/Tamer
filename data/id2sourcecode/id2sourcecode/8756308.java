    public boolean readImageFile() {
        ByteBuffer byteBuffer;
        FileChannel fileChannel;
        FileInputStream fileInputStream;
        byte[] bufData;
        try {
            bufData = new byte[this.x * this.y / Byte.SIZE];
            byteBuffer = ByteBuffer.wrap(bufData);
            fileInputStream = new FileInputStream(filepath);
            fileChannel = fileInputStream.getChannel();
            int counter = 0;
            for (int i = 0; i < this.z; i++) {
                fileChannel.position(i * this.x * this.y / Byte.SIZE);
                byteBuffer.position(0);
                int pos = byteBuffer.position();
                byteBuffer.position(0);
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    image[counter++] = b;
                }
                byteBuffer.position(pos);
            }
            fileChannel.close();
            fileInputStream.close();
            System.out.println("Voxels read = " + counter);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
