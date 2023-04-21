    public void writeContents(InputStream in, boolean overwrite) throws ArchteaEntryException {
        File f = new File(m_path);
        if ((!overwrite) && f.exists()) {
            throw new ArchteaEntryException("Cannot overwrite it because a file already exists.");
        } else if (!f.canWrite()) {
            throw new ArchteaEntryException("Cannot write a file.");
        } else if (in == null) {
            throw new ArchteaEntryException("Input stream is null.");
        }
        m_initAttributes = false;
        BufferedOutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(f), getBuffSize());
            in = new BufferedInputStream(in, getBuffSize());
            byte[] buff = new byte[getBuffSize()];
            int len = in.read(buff);
            while (len != -1) {
                out.write(buff, 0, len);
                len = in.read(buff);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ArchteaEntryException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
