    public static void main(String args[]) {
        ReadWriterWithBuffer rw = new ReadWriterWithBuffer();
        try {
            long startTime = System.currentTimeMillis();
            rw.readWrite("D:/setup.exe", "D:/myeclipse1.exe");
            long endTime = System.currentTimeMillis();
            System.out.println("Direct read and write time: " + (endTime - startTime) + "ms");
            startTime = System.currentTimeMillis();
            rw.readWriterBuffer("D:/setup.exe", "D:/myeclipse2.exe");
            endTime = System.currentTimeMillis();
            System.out.println("Buffer read and write time: " + (endTime - startTime) + "ms");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
