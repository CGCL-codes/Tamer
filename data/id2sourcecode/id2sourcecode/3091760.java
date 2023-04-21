    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(localPath.getName());
        out.writeLong(localPath.length());
        byte[] xfer = new byte[4096];
        FileInputStream fis = new FileInputStream(localPath);
        try {
            for (int read = fis.read(xfer); read >= 0; read = fis.read(xfer)) out.write(xfer, 0, read);
        } finally {
            fis.close();
        }
    }
