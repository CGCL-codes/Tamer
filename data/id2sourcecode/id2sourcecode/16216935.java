        public void run() {
            String reqUrl = REST_URI + "/db?_query=" + URLEncoder.encode("collection('/db')//SPEECH[SPEAKER = 'JULIET']");
            for (int i = 0; i < 200; i++) {
                try {
                    URL url = new URL(reqUrl);
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.setRequestMethod("GET");
                    connect.connect();
                    int r = connect.getResponseCode();
                    assertEquals("Server returned response code " + r, 200, r);
                    System.out.println(readResponse(connect.getInputStream()));
                    synchronized (this) {
                        wait(250);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }
            }
        }
