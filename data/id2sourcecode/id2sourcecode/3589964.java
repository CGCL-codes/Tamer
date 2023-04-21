        public void send(String urlstring) {
            BufferedReader reader = null;
            try {
                URL url = new URL(urlstring);
                URLConnection connection = url.openConnection();
                connection.connect();
                reader = getReader(connection);
                String result = null;
                do {
                    result = reader.readLine();
                } while (result != null);
                reader.close();
            } catch (MalformedURLException e) {
                _log.error("send(): caught MalformedURLException: " + e.getMessage(), e);
            } catch (IOException e) {
                _log.error("send(): caught IOException: " + e.getMessage(), e);
            }
        }
