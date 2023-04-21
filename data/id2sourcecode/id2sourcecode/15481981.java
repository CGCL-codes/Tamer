                    public void run() {
                        try {
                            URL url = new URL("http://localhost:8888/servlet/GetFileService?id=9");
                            InputStream is = url.openConnection().getInputStream();
                            is.close();
                        } catch (Throwable t) {
                        }
                    }
