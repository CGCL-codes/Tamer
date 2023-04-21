    private static void write() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("d:\\test.txt");
            FileChannel fch = fos.getChannel();
            fch.lock();
            ByteBuffer srcs = ByteBuffer.allocate(2);
            srcs.put((byte) 5);
            srcs.put((byte) 6);
            fch.write(new ByteBuffer[] { srcs }, 2, 2);
            fos.write(Integer.MAX_VALUE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
