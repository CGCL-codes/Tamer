    protected void addFile(File newEntry, String name) {
        if (newEntry.isDirectory()) {
            return;
        }
        try {
            JarEntry je = new JarEntry(name);
            mJos.putNextEntry(je);
            FileInputStream fis = new FileInputStream(newEntry);
            byte fdata[] = new byte[1024];
            int readCount = 0;
            while ((readCount = fis.read(fdata)) != -1) {
                mJos.write(fdata, 0, readCount);
            }
            fis.close();
            mJos.closeEntry();
            mObserverCont.setNext(je);
            mObserverCont.setCount(++miCurrentCount);
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        }
    }
