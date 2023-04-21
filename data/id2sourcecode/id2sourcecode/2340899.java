    public static void main(String argv[]) throws Exception {
        int b;
        ContentLengthCounter os = new ContentLengthCounter();
        while ((b = System.in.read()) >= 0) os.write(b);
        System.out.println("size " + os.getSize());
    }
