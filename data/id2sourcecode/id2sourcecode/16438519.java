    private void grabFrame(GL gl, int x, int y, int width, int height, String theFileName) {
        try {
            try {
                File myParent = new File(theFileName).getParentFile();
                if (!myParent.exists()) {
                    myParent.mkdirs();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            RandomAccessFile out = new RandomAccessFile(new File(theFileName), "rw");
            FileChannel ch = out.getChannel();
            int fileLength = TARGA_HEADER_SIZE + width * height * 3;
            out.setLength(fileLength);
            MappedByteBuffer image = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
            image.put(0, (byte) 0).put(1, (byte) 0);
            image.put(2, (byte) 2);
            image.put(12, (byte) (width & 0xFF));
            image.put(13, (byte) (width >> 8));
            image.put(14, (byte) (height & 0xFF));
            image.put(15, (byte) (height >> 8));
            image.put(16, (byte) 24);
            image.position(TARGA_HEADER_SIZE);
            ByteBuffer bgr = image.slice();
            gl.glReadPixels(x, y, width, height, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, bgr);
            ch.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
