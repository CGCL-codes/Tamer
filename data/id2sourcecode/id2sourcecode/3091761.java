    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String remote = in.readUTF();
        String ext = ".data";
        int dot = remote.lastIndexOf('.');
        if (dot > 0) {
            ext = remote.substring(dot);
            remote = remote.substring(0, dot);
        }
        localPath = File.createTempFile(remote, ext);
        FileOutputStream fos = new FileOutputStream(localPath);
        try {
            long length = in.readLong();
            byte[] xfer = new byte[4096];
            while (length > 0) {
                int read = in.read(xfer, 0, (int) Math.min(xfer.length, length));
                if (read < 0) throw new EOFException("Unexpected EOF (" + length + " bytes remaining)");
                fos.write(xfer, 0, read);
                length -= read;
            }
        } finally {
            fos.close();
        }
    }
