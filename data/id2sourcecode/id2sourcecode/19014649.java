    public static boolean zip(final File[] _filesToZip, final File _zipFile) {
        if (_filesToZip == null || _zipFile == null || _filesToZip.length == 0 || canWrite(_zipFile) != null) {
            return false;
        }
        ZipOutputStream out = null;
        boolean ok = true;
        try {
            out = new ZipOutputStream(new FileOutputStream(_zipFile));
            for (int i = 0; i < _filesToZip.length; i++) {
                FileInputStream in = null;
                try {
                    in = new FileInputStream(_filesToZip[i]);
                    out.putNextEntry(new ZipEntry(_filesToZip[i].getName()));
                    if (!copyStream(in, out, false, false)) {
                        return false;
                    }
                    out.closeEntry();
                } catch (final IOException e) {
                    _zipFile.delete();
                    if (Fu.DEBUG && FuLog.isDebug()) {
                        FuLog.error(e);
                    }
                    ok = false;
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } catch (final IOException e) {
            _zipFile.delete();
            if (Fu.DEBUG && FuLog.isDebug()) {
                FuLog.error(e);
            }
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (final IOException e) {
                _zipFile.delete();
                if (Fu.DEBUG && FuLog.isDebug()) {
                    FuLog.error(e);
                }
                ok = false;
            }
        }
        return ok;
    }
