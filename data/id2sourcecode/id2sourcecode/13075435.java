            public void run() {
                InputStream input = null;
                URL url = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(trackingURL + tagID);
                } catch (MalformedURLException e) {
                    SensorTransformBehavior.this.setEnable(false);
                    e.printStackTrace();
                    return;
                }
                MessageDispatcher.getInstance().notifyStatus(bproxy.getName() + " is connecting to " + url);
                while (SensorTransformBehavior.this.getEnable()) {
                    try {
                        conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        input = (InputStream) conn.getContent();
                        parseCoords(input);
                        Thread.sleep(interval);
                    } catch (Exception e) {
                        conn.disconnect();
                        positions.clear();
                        SensorTransformBehavior.this.setEnable(false);
                        MessageDispatcher.getInstance().notifyError(bproxy.getName() + " reports " + e.toString());
                        return;
                    }
                }
            }
