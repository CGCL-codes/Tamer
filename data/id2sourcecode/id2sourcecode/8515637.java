    private final void write(Socket s) throws Exception {
        long sequenceNumber = getSequenceNumber();
        MessageDigest mdigest = (MessageDigest) MessageDigest.getInstance("SHA").clone();
        if (l.isLoggable(Level.INFO)) {
            l.log(Level.INFO, Thread.currentThread().toString() + ": WRITE START");
        }
        InputStream si = null;
        OutputStream so = null;
        try {
            si = s.getInputStream();
            so = s.getOutputStream();
            byte b[] = new byte[8192];
            String f = formatter.format(new Date()) + "_" + sequenceNumber;
            int li = si.read(b);
            if (li >= 0) {
                if (l.isLoggable(Level.INFO)) {
                    l.log(Level.INFO, Thread.currentThread().toString() + ": WRITE " + f);
                }
                FileOutputStream of = null;
                try {
                    of = new FileOutputStream(f);
                    of.write(b, 0, li);
                    mdigest.update(b, 0, li);
                    while ((li = si.read(b)) >= 0) {
                        mdigest.update(b, 0, li);
                        of.write(b, 0, li);
                    }
                    of.flush();
                } finally {
                    close(of);
                }
                byte[] digest = mdigest.digest();
                String id = toHexID(digest);
                File destfile = new File(id);
                if (destfile.exists()) {
                    new File(f).delete();
                    if (l.isLoggable(Level.INFO)) {
                        l.log(Level.INFO, Thread.currentThread().toString() + ": REMOVED " + f + " because hash " + id + " was already existing");
                    }
                    s.shutdownInput();
                    returnID(id, so);
                } else {
                    boolean rename = (new File(f)).renameTo(destfile);
                    if (rename) {
                        s.shutdownInput();
                        returnID(id, so);
                    } else {
                        throw new Exception("Could not rename " + f + " to " + destfile);
                    }
                }
            }
        } finally {
            close(so);
        }
        if (l.isLoggable(Level.INFO)) {
            l.log(Level.INFO, Thread.currentThread().toString() + ": WRITE END");
        }
    }
