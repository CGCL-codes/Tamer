    private void gwcRequestAddress() {
        try {
            if (gwcs.size() == 0) {
                URL url = new URL(Constants.URL_LIST_GWC);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if ((connection != null) && (connection.getResponseCode() == 200)) {
                    BufferedReader bin = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String hostCache;
                    Debug.log("[ProviderServentsAddress] Inizio inizializzazione GWC...");
                    try {
                        while ((hostCache = bin.readLine()) != null) gwcs.add(hostCache);
                    } catch (IOException e) {
                        bin.close();
                        throw e;
                    }
                    bin.close();
                    Debug.log("[ProviderServentsAddress] Inizializzazione GWC completata!");
                } else {
                    Debug.log("[ProviderServentsAddress] Problema nella lettura della lista degli GWebCache");
                }
            }
            Iterator iter = gwcs.iterator();
            while (iter.hasNext()) new GWebCache((String) iter.next());
            Debug.log("[ProviderServentsAddress] Richesta GWC completata!");
        } catch (Exception e) {
            Debug.log("[ProviderServentsAddress] Errore nella funzione di bootStrap...");
            Debug.log(e.getMessage());
        }
    }
