    public boolean isApplicationActive() {
        tmp = new File(System.getProperty("user.dir") + File.separator + appName + ".lock");
        try {
            channel = new RandomAccessFile(tmp, "rw").getChannel();
            try {
                lock = channel.tryLock();
                if (lock == null) {
                    close();
                    return true;
                }
                Runtime.getRuntime().addShutdownHook(new Thread(this));
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return true;
        }
    }
