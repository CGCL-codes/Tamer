    public void storeFiles(java.util.Map params, String dir) {
        java.util.Iterator it = params.keySet().iterator();
        File fDir = new java.io.File(dir);
        if (fDir.isDirectory()) {
            while (it.hasNext()) {
                Object key = it.next();
                try {
                    if (params.get(key) instanceof FileInfo) {
                        FileInfo file = (FileInfo) params.get(key);
                        File ftmp = new File(fDir, file.getClientFileName());
                        FileOutputStream fos = new FileOutputStream(ftmp);
                        FileChannel fch = fos.getChannel();
                        ReadableByteChannel rbch = Channels.newChannel(new ByteArrayInputStream(file.getFileContents()));
                        long pos = 0;
                        long cnt = 0;
                        java.nio.channels.FileLock flock = fch.lock();
                        while ((cnt = fch.transferFrom(rbch, pos, file.getFileContents().length)) > 0) {
                            pos += cnt;
                        }
                        flock.release();
                        fos.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            System.err.println("NO A DIRECTORY " + dir);
        }
    }
