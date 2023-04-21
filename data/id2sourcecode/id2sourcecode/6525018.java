    public void open() {
        try {
            fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();
            mappedByteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
