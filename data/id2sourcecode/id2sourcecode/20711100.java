    private void writeIndexPosition(long indexPosition, int nbytes) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.getChannel().position(indexPositionPosition);
            System.out.println("Index position position: " + indexPositionPosition);
            System.out.println("Index position: " + indexPosition);
            System.out.println("nBytes: " + nbytes);
            BufferedByteWriter buffer = new BufferedByteWriter();
            buffer.putLong(indexPosition);
            buffer.putInt(nbytes);
            raf.write(buffer.getBytes());
            raf.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
