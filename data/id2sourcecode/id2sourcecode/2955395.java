    private boolean download(URL target, File file) {
        LoggerUtil.addFine(_log, "JarDiffHandler:  Doing download");
        boolean ret = true;
        boolean delete = false;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(target.openStream());
            out = new BufferedOutputStream(new FileOutputStream(file));
            int read = 0;
            int totalRead = 0;
            byte[] buf = new byte[BUF_SIZE];
            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
                totalRead += read;
            }
            LoggerUtil.addFine(_log, "total read: " + totalRead);
            LoggerUtil.addFine(_log, "Wrote URL " + target.toString() + " to file " + file);
        } catch (IOException ioe) {
            LoggerUtil.addFine(_log, "Got exception while downloading resource: " + ioe);
            ret = false;
            if (file != null) delete = true;
        } finally {
            try {
                in.close();
                in = null;
            } catch (IOException ioe) {
                LoggerUtil.addFine(_log, "Got exception while downloading resource: " + ioe);
            }
            try {
                out.close();
                out = null;
            } catch (IOException ioe) {
                LoggerUtil.addFine(_log, "Got exception while downloading resource: " + ioe);
            }
            if (delete) {
                file.delete();
            }
        }
        return ret;
    }
