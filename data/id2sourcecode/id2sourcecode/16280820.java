    public static boolean test() {
        try {
            String filename = "com.orbs.util.filemap.FileMap_test.tmp";
            FileMap fileMap = new FileMap(new File("."));
            fileMap.put(filename, new ByteArrayInputStream("Hello".getBytes()));
            InputStream in = (InputStream) fileMap.get(filename);
            int i;
            while ((i = in.read()) > -1) System.out.write(i);
            in.close();
            System.out.println("");
            fileMap.remove(filename);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
