    protected boolean download(URL target, File file) {
        if (_log.isDebugLevel()) _log.debug("download(" + target + ") => " + file);
        boolean delete = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(ObjectUtil.openResource(target));
            out = new BufferedOutputStream(new FileOutputStream(file));
            int read = 0, totalRead = 0;
            final byte[] buf = new byte[BUF_SIZE];
            while ((read = in.read(buf)) != -1) {
                if (read > 0) {
                    out.write(buf, 0, read);
                    totalRead += read;
                }
            }
            if (_log.isDebugLevel()) _log.debug("download(" + target + ")[" + totalRead + " bytes] => " + file);
            return true;
        } catch (IOException ioe) {
            _log.warn("download(" + target + ") " + ioe.getClass().getName() + " while write to file=" + file + ": " + ioe.getMessage(), ioe);
            if (file != null) delete = true;
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException ioe) {
                    _log.warn("download(" + target + ") " + ioe.getClass().getName() + " while close input to file=" + file + ": " + ioe.getMessage(), ioe);
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException ioe) {
                    _log.warn("download(" + target + ") " + ioe.getClass().getName() + " while close output to file=" + file + ": " + ioe.getMessage(), ioe);
                }
            }
            if (delete) {
                if (!file.delete()) _log.warn("download(" + target + ") failed to delete file=" + file);
            }
        }
    }
