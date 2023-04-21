                        public void open(String mehod, URL url, boolean async) {
                            try {
                                URLConnection connection = url.openConnection();
                                bytes = new byte[connection.getContentLength()];
                                InputStream inputStream = connection.getInputStream();
                                inputStream.read(bytes);
                                inputStream.close();
                                for (ReadyStateChangeListener readyStateChangeListener : readyStateChangeListeners) {
                                    readyStateChangeListener.readyStateChanged();
                                }
                            } catch (IOException e) {
                            }
                        }
