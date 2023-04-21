    public static void benchmark(String filename, int bufferSize) {
        System.out.println("\nBenchmarking...");
        long time = (new Date()).getTime();
        try {
            RandomAccessFile inFile = new RandomAccessFile(filename, RandomAccessFile.READ, bufferSize);
            RandomAccessFile outFile = new RandomAccessFile("temp.data", RandomAccessFile.WRITE | RandomAccessFile.CREATE, bufferSize);
            try {
                while (true) {
                    outFile.writeByte(inFile.readByte());
                }
            } catch (EOFException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inFile.close();
                outFile.close();
            }
            System.out.println(". RandomAccessFile elapsed time=" + ((new Date()).getTime() - time));
            time = (new Date()).getTime();
            java.io.RandomAccessFile inFile2 = new java.io.RandomAccessFile(filename, "r");
            java.io.RandomAccessFile outFile2 = new java.io.RandomAccessFile("temp.data", "rw");
            try {
                while (true) {
                    outFile2.writeByte(inFile2.readByte());
                }
            } catch (EOFException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inFile2.close();
                outFile2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(". java.io.RandomAccessFile elapsed time=" + ((new Date()).getTime() - time));
    }
