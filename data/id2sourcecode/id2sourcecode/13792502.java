    public static void main(String[] args) throws IOException {
        long max = 0, mean = 0, min = Long.MAX_VALUE;
        for (int i = 0; i < 1; i++) {
            File f = new File("exemple2.txt");
            FileReader fr = new FileReader(f);
            int nbread = -1;
            char bytes[] = new char[1024];
            StringBuffer buf = new StringBuffer();
            while ((nbread = fr.read(bytes)) >= 0) {
                buf.append(bytes, 0, nbread);
            }
            fr.close();
            long start = System.currentTimeMillis();
            new RegExpParser().parse(buf);
            long stop = System.currentTimeMillis();
            long value = stop - start;
            if (value > max) {
                max = value;
            }
            if (value < min) {
                min = value;
            }
            mean = (max + min) / 2;
        }
        System.out.println("Max : " + max + " ms");
        System.out.println("Mean : " + mean + " ms");
        System.out.println("Min : " + min + " ms");
    }
