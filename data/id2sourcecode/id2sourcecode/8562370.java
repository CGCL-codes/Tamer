        protected static File createTempDir() throws IOException {
            String[] tmpnames;
            Random random;
            File tmpdir;
            File tmpsys;
            String msg;
            int cnt;
            int i;
            tmpnames = new String[] { System.getProperty("java.io.tmpdir"), System.getProperty("user.home"), System.getProperty("user.dir") };
            tmpdir = null;
            for (i = 0; i < tmpnames.length; i++) {
                tmpsys = new File(tmpnames[i]);
                if (tmpsys.canRead() && tmpsys.canWrite()) {
                    random = new Random();
                    cnt = 1000;
                    while (cnt-- > 0) {
                        tmpdir = new File(tmpsys, String.valueOf(random.nextInt(Integer.MAX_VALUE)));
                        if (tmpdir.mkdir()) {
                            log2_.debug("Successfully created '" + tmpdir + "'");
                            return tmpdir;
                        }
                        log2_.debug("Creating '" + tmpdir + "' failed");
                    }
                    msg = "Could not create directory within '" + tmpsys + "'";
                    log2_.error(msg);
                    throw new IOException(msg);
                }
            }
            msg = "Could not find directory with read/write permission";
            log2_.error(msg);
            throw new IOException(msg);
        }
