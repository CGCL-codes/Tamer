    private IFile copyFileToCache(IFile boxFile, String filename) {
        if (logger.isDebugEnabled()) {
            logger.debug("copyFileToCache(IFile, String) - start");
        }
        IFile result = null;
        try {
            InputStream in = boxFile.getInput();
            File cacheFile = getCacheFile(filename);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(cacheFile));
            int read = in.read();
            while (read >= 0) {
                out.write(read);
                read = in.read();
            }
            out.close();
            in.close();
            result = new FsFile(cacheFile);
        } catch (IOException e) {
            logger.error("copyFileToCache(IFile, String)", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("copyFileToCache(IFile, String) - end");
        }
        return result;
    }
