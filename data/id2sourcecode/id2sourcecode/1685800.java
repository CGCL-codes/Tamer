    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("C:/Temp/raf.txt", "rw");
        FileChannel ch = raf.getChannel();
        System.out.println("FileChannel from RandomAccessFile : " + ch);
        raf.close();
        FileInputStream fis = new FileInputStream("C:/Temp/raf.txt");
        ch = fis.getChannel();
        System.out.println("FileChannel from FileInputStream : " + ch);
        fis.close();
    }
