        public void test() throws IOException {
            FileChannel fc = new RandomAccessFile(new File("temp.tmp"), "rw").getChannel();
            IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
            ib.put(0);
            for (int i = 1; i < numOfUbuffInts; i++) ib.put(ib.get(i - 1));
            fc.close();
        }
