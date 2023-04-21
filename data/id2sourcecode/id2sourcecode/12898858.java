    public static void processhapmap() {
        int chromosome = 8;
        File inputDir = new File("C:/Projects/NAM/hapmap");
        String inName = "chr" + chromosome + ".CSHLALLBGI.h90_f50.Q87Q87Union.imp.hmp.txt";
        try {
            FileInputStream fis = new FileInputStream(new File(inputDir, inName));
            FileChannel infc = fis.getChannel();
            int capacity = 10240;
            byte[] input = new byte[capacity];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
