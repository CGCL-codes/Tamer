        private void hostFileRequest() {
            try {
                URL url = new URL(addr + "/?client=" + Constants.FUSTEENO_NAME + "&version=1.0&hostfile=1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(Constants.CONNECT_SOCKET_TIMEOUT);
                connection.setRequestMethod("GET");
                connection.connect();
                if ((connection != null) && (connection.getResponseCode() == 200)) {
                    BufferedReader bin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String node;
                    try {
                        while ((node = bin.readLine()) != null) pushAddress(node);
                    } catch (IOException e) {
                        Debug.log("[GWebCache] Errore nella lettura della response del GWebCache...");
                        bin.close();
                        throw e;
                    }
                    bin.close();
                }
            } catch (Exception e) {
                Debug.log("[GWebCache] Errore nella richiesta della lista all'hostCache " + addr + ":");
                Debug.log(e.getMessage());
            }
        }
