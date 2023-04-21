    public void run() {
        try {
            fireUpdate(DownloadObserver.BEGINNING, null);
            fireUpdate(DownloadObserver.CONTENT_LENGTH, src.openConnection().getContentLength());
            ZipInputStream zin = new ZipInputStream(src.openStream());
            ZipEntry ze;
            byte[] buf = new byte[2048];
            int read;
            while ((ze = zin.getNextEntry()) != null) {
                File outFile = new File(dest, ze.getName());
                outFile.createNewFile();
                FileOutputStream fout = new FileOutputStream(outFile);
                fireUpdate(DownloadObserver.NEW_FILE, outFile);
                while ((read = zin.read(buf)) != -1) {
                    fout.write(buf, 0, read);
                    fireUpdate(DownloadObserver.PROGRESS, new Object[] { buf, 0, read });
                }
                fout.close();
            }
            zin.close();
            fireUpdate(DownloadObserver.END, null);
        } catch (Exception ex) {
            fireUpdate(DownloadObserver.ERROR, ex);
        }
        synchronized (lock) {
            lock.notifyAll();
        }
    }
