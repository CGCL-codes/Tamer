        private boolean pingRequest() {
            try {
                URL url = new URL(addr + "/?client=" + Constants.FUSTEENO_NAME + "&version=1.0&ping=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(Constants.CONNECT_SOCKET_TIMEOUT);
                connection.setRequestMethod("GET");
                connection.connect();
                if ((connection != null) && (connection.getResponseCode() == 200)) {
                    BufferedReader bin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String response;
                    try {
                        if (((response = bin.readLine()) == null) || !response.contains("PONG")) {
                            bin.close();
                            return false;
                        }
                    } catch (IOException e) {
                        Debug.log("[ProviderServentsAddress] Errore nella lettura della response...");
                        bin.close();
                        return false;
                    }
                } else {
                    Debug.log("[GWebCache] Responde: " + connection.getResponseMessage());
                    return false;
                }
            } catch (Exception e) {
                Debug.log("[GWebCache] Errore nel ping del GWC '" + addr + "':");
                Debug.log(e.getMessage());
                return false;
            }
            return true;
        }
