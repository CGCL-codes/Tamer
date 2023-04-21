    @Override
    public void unLoadView(final String path) {
        if (serverIPAddress != null && !serverIPAddress.equals("")) {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    try {
                        String urlStr = "http://" + serverIPAddress + ":8080" + "/unloadview?View=" + path + "&Port=" + port;
                        URL url = new URL(urlStr);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        if (handler.isDebugMode()) {
                            System.out.println("unloading view=" + path);
                        }
                        connection.connect();
                        connection.getContent();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }
