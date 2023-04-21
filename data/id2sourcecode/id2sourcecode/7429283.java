    public static void writeFile(byte[] fileBuffer, String fullDestPath, int position, boolean complete) throws Exception {
        String partFileName = fullDestPath + "-part";
        ByteBuffer rBuf = null;
        rBuf = ByteBuffer.allocate(fileBuffer.length);
        rBuf.put(fileBuffer);
        rBuf.flip();
        RandomAccessFile fos = new RandomAccessFile(partFileName, "rw");
        FileChannel channel = fos.getChannel();
        int written = 0;
        if (position == -1) {
            written = channel.write(rBuf);
        } else {
            written = channel.write(rBuf, (long) position);
        }
        fos.close();
        channel.close();
        System.out.println("Written " + written + " bytes from " + (long) position);
        System.out.println("Transfered  " + (position + written) + " bytes >>> " + partFileName);
        if (complete) {
            System.out.println((position + written) + " bytes transfered, Transfer completed!");
            File des = new File(fullDestPath);
            if (des.exists()) {
                System.out.println("Target file " + fullDestPath + " alread exist, deleting the file first");
                if (des.delete()) {
                    System.out.println("File " + fullDestPath + " deleted");
                } else {
                    System.out.println("Failed deleting file " + fullDestPath);
                }
            }
            File src = new File(partFileName);
            src.renameTo(des);
            System.out.println("File is stored in >>> " + fullDestPath);
        }
    }
