        private static Double getLastMtGoxTrade(String currency) {
            try {
                URL url = new URL("https://mtgox.com/api/1/BTC" + currency + "/public/ticker");
                HttpsURLConnection connection;
                connection = (HttpsURLConnection) url.openConnection();
                connection.setHostnameVerifier(HOST_NAME_VERIFIER);
                connection.setSSLSocketFactory(SSL_SOCKET_FACTORY);
                connection.setReadTimeout(60000);
                connection.setRequestMethod("GET");
                connection.connect();
                int status = connection.getResponseCode();
                if (status != 200) {
                    return null;
                }
                String ticker = StreamReader.readFully(connection.getInputStream());
                int index = ticker.indexOf(LAST);
                if (index == -1) {
                    return null;
                }
                index += LAST.length();
                index = ticker.indexOf(VALUE, index);
                if (index == -1) {
                    return null;
                }
                index += VALUE.length();
                index = ticker.indexOf('"', index);
                if (index == -1) {
                    return null;
                }
                index += 1;
                int startIndex = index;
                index = ticker.indexOf('"', index);
                if (index == -1) {
                    return null;
                }
                int endIndex = index - 1;
                if (endIndex <= startIndex) {
                    return null;
                }
                String value = ticker.substring(startIndex, endIndex);
                return Double.parseDouble(value);
            } catch (Exception e) {
                return null;
            }
        }
