    public boolean load(String name) {
        try {
            FileInputStream fs = new FileInputStream(name);
            byte[] buf = new byte[1024];
            int len;
            while ((len = fs.read(buf)) >= 0) write(buf, 0, len);
            fs.close();
            return true;
        } catch (IOException exx) {
            exx.printStackTrace();
        }
        return false;
    }
