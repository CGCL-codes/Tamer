    public void read(String name, OutputStream out) throws SystemException {
        File file = new File(folder, name);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[2048];
            int t;
            while ((t = in.read(buf)) > 0) out.write(buf, 0, t);
            out.flush();
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
