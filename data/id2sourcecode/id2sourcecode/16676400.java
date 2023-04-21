    private IntBuffer getIndex() {
        if (mIndex == null) {
            File file = new File(indexname);
            if (file.exists()) {
                try {
                    FileChannel roChannel = new RandomAccessFile(file, "rw").getChannel();
                    ByteBuffer buf = roChannel.map(FileChannel.MapMode.PRIVATE, 0, (int) roChannel.size());
                    buf.clear();
                    mIndex = buf.asIntBuffer();
                } catch (FileNotFoundException ex) {
                    Tools.logException(FileList.class, ex);
                    ex.printStackTrace();
                } catch (IOException ex) {
                    Tools.logException(FileList.class, ex);
                    ex.printStackTrace();
                }
            }
        }
        System.out.println(mIndex);
        return mIndex;
    }
