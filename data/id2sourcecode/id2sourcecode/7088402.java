    public static void saveEntity(HttpEntity entity, String fileName) {
        long size = entity.getContentLength();
        File file = new File(fileName);
        byte buffer[];
        if (size > MAX_BUFFER_SIZE) {
            buffer = new byte[MAX_BUFFER_SIZE];
        } else {
            buffer = new byte[(int) size];
        }
        InputStream streamEntity;
        RandomAccessFile outFile;
        try {
            streamEntity = entity.getContent();
            outFile = new RandomAccessFile(file, "rw");
            int read = 0;
            outFile.setLength(0);
            System.out.println("Buffer size =" + buffer.length);
            while ((read = streamEntity.read(buffer)) != -1) {
                System.out.println("write!");
                outFile.write(buffer, 0, read);
            }
            outFile.close();
            streamEntity.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
