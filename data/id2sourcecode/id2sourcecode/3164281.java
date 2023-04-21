    public static String read(Context context) throws RuntimeException {
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(Constants.DATA_FILE);
            FileChannel fc = inputStream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            String fileContent = Charset.forName(Constants.ENCODING).decode(bb).toString();
            inputStream.close();
            return fileContent;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
