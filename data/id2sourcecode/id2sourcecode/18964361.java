    public void loadFromFile(RandomAccessFile file) {
        try {
            MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            byte[] arr = new byte[(int) file.length()];
            buffer.get(arr);
            currentResult = new BigInteger(arr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
