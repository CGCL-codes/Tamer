    public void transcode(final Recording recording) {
        final File inputPipe = new File("/tmp/recman/", System.currentTimeMillis() + recording.getId() + ".pipe");
        inputPipe.deleteOnExit();
        String cmdArray[] = new String[] { "mkfifo", "--mode=777", inputPipe.getAbsolutePath() };
        ProcessBuilder builder = new ProcessBuilder(cmdArray);
        Process p;
        try {
            p = builder.start();
            p.waitFor();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MediaDescriptor mediaDescriptor = new MediaDescriptor(jvlc, inputPipe.getAbsolutePath());
        mediaDescriptor.addOption(":sout=#transcode{width=320,height=240,vcodec=mp2v,acodec=mpga,vb=1000,ab=128}:std{access=file,mux=ts,dst=out.mpg}");
        MediaPlayer mp = mediaDescriptor.getMediaPlayer();
        mp.play();
        new Thread() {

            @Override
            public void run() {
                RandomAccessRecording in = null;
                FileOutputStream out = null;
                try {
                    in = new RandomAccessRecording(RecordingFormat.getAbsolutePaths(new File(recording.getPath()), recording.getParts(), recording.isTs()));
                    out = new FileOutputStream(inputPipe);
                    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                    int read;
                    while ((read = in.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        out.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        int retry = 0;
        while (!mp.isPlaying()) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }
            retry++;
            if (retry > 12) {
            }
        }
        while (mp.isPlaying()) {
        }
    }
