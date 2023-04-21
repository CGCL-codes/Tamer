            public void run() {
                try {
                    URLConnection conn = url.openConnection();
                    conn.getInputStream().close();
                } catch (IOException e) {
                }
                httpRequestRunning = false;
            }
