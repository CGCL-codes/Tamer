    private void serializeBlock() throws Exception, Throwable {
        System.out.println(LanguageTraslator.traslate("489") + blockFileName);
        File file = new File(blockFileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(blockFileName);
        FileChannel channel = fileOutputStream.getChannel();
        FileLock lock = channel.tryLock();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(matrix);
        matrix = null;
        lock.release();
        objectOutputStream.flush();
        fileOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }
