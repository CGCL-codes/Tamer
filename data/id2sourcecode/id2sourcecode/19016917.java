    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream("out.dat");
        FileChannel fc = in.getChannel();
        fc.close();
        int n = in.read();
        in.close();
    }
