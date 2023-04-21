    public void run() {
        try {
            byte[] buff = new byte[1024];
            String dir = this.fileName.substring(0, this.fileName.lastIndexOf(System.getProperty("file.separator")));
            File file = new File(dir);
            if (!file.exists()) file.mkdirs();
            System.out.println(dir);
            System.out.println(fileName);
            FileOutputStream fos = new FileOutputStream(this.fileName);
            int readCount = data.read(buff);
            while (readCount >= 0) {
                fos.write(buff, 0, readCount);
                readCount = data.read(buff);
            }
        } catch (Exception e) {
            log.error(this, e);
        }
    }
