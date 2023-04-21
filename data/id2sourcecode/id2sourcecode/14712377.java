    public void write(InputStream in) {
        byte[] buffer = new byte[0xFFFF];
        try {
            FileOutputStream out = new FileOutputStream(file);
            for (int len; (len = in.read(buffer)) != -1; ) out.write(buffer, 0, len);
        } catch (IOException error) {
            Log.print(error);
        }
    }
