    public void saveToFile(RandomAccessFile file) {
        try {
            byte[] arr = currentResult.toByteArray();
            MappedByteBuffer buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, arr.length);
            buffer.put(arr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
