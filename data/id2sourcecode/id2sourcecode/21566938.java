        private void assertConnection() {
            URL url = createMapRequestURL();
            if (url != null) {
                try {
                    if (!WMS_Connections.containsKey(url.toString())) {
                        HttpURLConnection urlc;
                        urlc = (HttpURLConnection) url.openConnection();
                        urlc.setReadTimeout(Navigator.TIME_OUT);
                        InputStream is = urlc.getInputStream();
                        is.close();
                        WMS_Connections.put(url.toString(), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
