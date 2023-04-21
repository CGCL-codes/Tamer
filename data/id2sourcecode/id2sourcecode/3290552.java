        public void test() throws IOException {
            FileChannel fc = new FileInputStream(new File("temp.tmp")).getChannel();
            IntBuffer ib = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).asIntBuffer();
            while (ib.hasRemaining()) ib.get();
            fc.close();
        }
