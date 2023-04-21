            public void run() {
                try {
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();
                    Object obj = conn.getContent();
                    url = new URL(link);
                    conn = url.openConnection();
                    obj = conn.getContent();
                } catch (MalformedURLException e) {
                    fail("MalformedURLException was thrown: " + e.toString());
                } catch (IOException e) {
                    fail("IOException was thrown.");
                }
            }
