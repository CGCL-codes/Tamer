            @Override
            public void run() {
                if (serverIPAddress != null && !serverIPAddress.equals("")) {
                    try {
                        String urlStr = "http://" + serverIPAddress + ":8080" + "/loadview?View=" + path + "&Port=" + port;
                        URL url = new URL(urlStr);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        if (handler.isDebugMode()) {
                            System.out.println("loading view=" + path);
                        }
                        connection.connect();
                        connection.getContent();
                    } catch (Exception ex) {
                        invalidateAllTags();
                        ex.printStackTrace();
                    }
                }
            }
