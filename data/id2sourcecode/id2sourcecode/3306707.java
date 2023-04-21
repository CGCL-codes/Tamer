    public void saveFile(File file) throws IOException, MessagingException {
        OutputStream out = null;
        InputStream in = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            in = this.getInputStream();
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ex) {
            }
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
            }
        }
    }
