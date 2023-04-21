    public synchronized void commit() throws IOException {
        File data = new File(dir, SFILE_DATA);
        if (data.exists()) {
            return;
        }
        Enumeration<File> closedFiles = closedTmpFiles.elements();
        while (closedFiles.hasMoreElements()) {
            File pdata = closedFiles.nextElement();
            FileInputStream in = new FileInputStream(pdata);
            byte buf[] = BufferPool.getInstance().get(1024);
            int len;
            md.reset();
            try {
                while ((len = in.read(buf)) != -1) {
                    md.update(buf, 0, len);
                }
            } finally {
                BufferPool.getInstance().put(buf);
                in.close();
            }
            String dg = Utils.formatDigest(md.digest());
            if (!dg.equals(digest)) {
                Utils.log(LOG_WARNING, pdata.getAbsolutePath() + " checksum mismatch: " + dg + " should be " + digest);
                continue;
            } else if (pdata.renameTo(data)) {
                break;
            } else {
                throw new IOException("Rename operation failed for " + data.getAbsolutePath());
            }
        }
        if (!data.exists()) {
            throw new IOException("Commit operation failed for " + data.getAbsolutePath());
        }
        closedFiles = closedTmpFiles.elements();
        while (closedFiles.hasMoreElements()) {
            File f = closedFiles.nextElement();
            f.delete();
        }
    }
