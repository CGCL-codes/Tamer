    private MediaPlayer createMediaPlayer(InputStream stream) throws IOException, FileNotFoundException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        if (stream == null) return null;
        File temp = File.createTempFile("mediaplayertmp", "dat");
        mTempPath = temp.getAbsolutePath();
        FileOutputStream out = new FileOutputStream(temp);
        byte buf[] = new byte[128];
        do {
            int numread = stream.read(buf);
            if (numread <= 0) break;
            out.write(buf, 0, numread);
        } while (true);
        MediaPlayer mp = new MediaPlayer();
        return mp;
    }
