        public void test() throws IOException {
            RandomAccessFile raf = new RandomAccessFile(new File("temp.tmp"), "rw");
            raf.writeInt(1);
            for (int i = 0; i < numOfUbuffInts; i++) {
                raf.seek(raf.length() - 4);
                raf.writeInt(raf.readInt());
            }
            raf.close();
        }
