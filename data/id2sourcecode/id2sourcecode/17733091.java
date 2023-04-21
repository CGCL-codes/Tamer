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
