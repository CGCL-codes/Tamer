    public long saveEntity(HttpEntity entity, String fileName) {
        long size = 0L;
        File file = new File(fileName);
        byte buffer[] = new byte[MAX_BUFFER_SIZE];
        InputStream streamEntity;
        RandomAccessFile outFile;
        try {
            streamEntity = entity.getContent();
            outFile = new RandomAccessFile(file, "rw");
            int read = 0;
            outFile.setLength(0);
            System.out.println("Buffer size =" + buffer.length);
            while ((read = streamEntity.read(buffer)) != -1) {
                size = size + read;
                outFile.write(buffer, 0, read);
            }
            outFile.close();
            streamEntity.close();
        } catch (IOException ex) {
            Logger.getLogger(HttpFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(HttpFileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return size;
    }
