    public static InputStream getStream(String uri, DefaultApplication application) throws IOException {
        if (uri.toLowerCase().endsWith(".http.mp3")) {
            log.debug("getStream: " + uri + ", " + application);
            boolean hasmore = false;
            int counter = 1;
            try {
                String id = Tools.extractName(Tools.extractName(uri));
                Audio audio = AudioManager.retrieveAudio(Integer.parseInt(id));
                do {
                    log.debug("getStream: audio=" + audio.getPath());
                    try {
                        class TimedThread implements Callable {

                            private DefaultApplication mApplication = null;

                            private String mPath = null;

                            public TimedThread(String path, DefaultApplication application) {
                                mPath = path;
                                mApplication = application;
                            }

                            public synchronized java.lang.Object call() throws java.lang.Exception {
                                URL url = new URL(mPath);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestProperty("Icy-Metadata", "1");
                                conn.setRequestProperty("User-Agent", "WinampMPEG/5.0");
                                conn.setRequestProperty("Accept", "audio/mpeg");
                                conn.setInstanceFollowRedirects(true);
                                try {
                                    IcyInputStream input = new IcyInputStream(conn.getInputStream());
                                    final IcyListener icyListener = new IcyListener(mApplication);
                                    input.addTagParseListener(icyListener);
                                    return input;
                                } catch (Throwable ex) {
                                    Tools.logException(Mp3Url.class, ex, mPath);
                                }
                                return conn.getInputStream();
                            }
                        }
                        TimedThread timedThread = new TimedThread(audio.getPath(), application);
                        TimedCallable timedCallable = new TimedCallable(timedThread, 1000 * 10);
                        InputStream mp3Stream = (InputStream) timedCallable.call();
                        if (mp3Stream != null) return new BufferedInputStream(mp3Stream, 1024 * 100);
                    } catch (Exception ex) {
                        if (audio != null && counter < 5) {
                            try {
                                List<Audio> list = AudioManager.findByTitleOrigenGenreExternalId(audio.getTitle(), audio.getOrigen(), audio.getGenre(), String.valueOf(++counter));
                                if (list != null && list.size() > 0) {
                                    audio = list.get(0);
                                    log.debug("Trying alternate: " + audio.getPath());
                                    hasmore = true;
                                }
                            } catch (Exception ex2) {
                                Tools.logException(Mp3Url.class, ex, uri);
                            }
                        } else Tools.logException(Mp3Url.class, ex, uri);
                    }
                } while (hasmore);
            } catch (Exception ex) {
                Tools.logException(Mp3Url.class, ex, uri);
            }
        }
        return Mp3Url.class.getResourceAsStream("/couldnotconnect.mp3");
    }
