    static void copy(InputStream in, OutputStream out) {
        byte[] buffer = new byte[0xFFFF];
        try {
            for (int len; (len = in.read(buffer)) != -1; ) out.write(buffer, 0, len);
        } catch (IOException error) {
            Log.print(error);
        }
    }
