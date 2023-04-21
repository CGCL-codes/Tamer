        public void test() throws IOException {
            FileChannel fc = new RandomAccessFile("temp.tmp", "rw").getChannel();
            IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
            for (int i = 0; i < numOfInts; i++) ib.put(i);
            fc.close();
        }
