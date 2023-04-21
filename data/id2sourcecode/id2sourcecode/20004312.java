    private static synchronized void speech(final Context context, final String text, final String language) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    final String encodedUrl = Constants.URL + language + "&q=" + URLEncoder.encode(text, Encoding.UTF_8.name());
                    final DefaultHttpClient client = new DefaultHttpClient();
                    HttpParams params = new BasicHttpParams();
                    params.setParameter("http.protocol.content-charset", "UTF-8");
                    client.setParams(params);
                    final FileOutputStream fos = context.openFileOutput(Constants.MP3_FILE, Context.MODE_WORLD_READABLE);
                    try {
                        try {
                            final HttpResponse response = client.execute(new HttpGet(encodedUrl));
                            downloadFile(response, fos);
                        } finally {
                            fos.close();
                        }
                        final String filePath = context.getFilesDir().getAbsolutePath() + "/" + Constants.MP3_FILE;
                        final MediaPlayer player = MediaPlayer.create(context.getApplicationContext(), Uri.fromFile(new File(filePath)));
                        player.start();
                        Thread.sleep(player.getDuration());
                        while (player.isPlaying()) {
                            Thread.sleep(100);
                        }
                        player.stop();
                    } finally {
                        context.deleteFile(Constants.MP3_FILE);
                    }
                } catch (InterruptedException ie) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
