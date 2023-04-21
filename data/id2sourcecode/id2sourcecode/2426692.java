    public String getCurrentIp() {
        String site = sites[getCurrentIndex()];
        rotateCurrentIndex();
        Printer.debug("Checking IP with: " + site);
        try {
            URL url = new URL(site);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            String fullResult = null;
            while ((line = in.readLine()) != null) {
                fullResult = fullResult + line;
            }
            connection.disconnect();
            String currentIp = IPValidator.findIp(fullResult);
            if (currentIp == null) {
                throw new IOException("Failed to determine current IP");
            }
            Printer.debug("IP resolved to: " + currentIp);
            retries = size - 1;
            return currentIp;
        } catch (IOException e) {
            if (retries > 0) {
                Printer.debug("Failed to resolve IP, retrying...");
                retries--;
                return getCurrentIp();
            } else {
                Printer.error(e.getMessage());
                retries = size - 1;
                return null;
            }
        }
    }
