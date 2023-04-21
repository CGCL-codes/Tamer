    public void create(String name, InputStream in) throws SystemException {
        assert !StringUtil.isEmpty(name);
        FileOutputStream out = null;
        File file = new File(folder, name);
        file.getParentFile().mkdirs();
        try {
            if (!file.exists()) file.createNewFile();
            out = new FileOutputStream(file);
            byte[] buf = new byte[2048];
            int t = 0;
            while ((t = in.read(buf)) > 0) out.write(buf, 0, t);
            out.flush();
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e1) {
            }
        }
    }
