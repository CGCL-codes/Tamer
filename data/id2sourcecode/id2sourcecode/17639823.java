    private void open(final String fileName) throws Exception {
        try {
            fileChannel = new FileInputStream(new File(fileName)).getChannel();
            shortBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).asShortBuffer();
        } catch (final Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
        System.out.println("open " + fileName);
        _exists = true;
    }
