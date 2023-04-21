    boolean copyExternalFile(File efile) {
        File pdata = new File(dir, SFILE_PARTIAL_DATA);
        try {
            FileInputStream in = new FileInputStream(efile);
            FileOutputStream out = new FileOutputStream(pdata);
            byte buf[] = BufferPool.getInstance().get(1024);
            int len;
            md.reset();
            try {
                try {
                    while ((len = in.read(buf)) != -1) {
                        md.update(buf, 0, len);
                        out.write(buf, 0, len);
                    }
                } finally {
                    out.close();
                }
            } finally {
                BufferPool.getInstance().put(buf);
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        String pdigest = Utils.formatDigest(md.digest());
        if (pdigest.equals(digest)) {
            return pdata.renameTo(new File(dir, SFILE_DATA));
        } else {
            Utils.log(LOG_ERROR, "Checksum mismatch in copyExternalFile: " + digest);
            pdata.delete();
        }
        return false;
    }
