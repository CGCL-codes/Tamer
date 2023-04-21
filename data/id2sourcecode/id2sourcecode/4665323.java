    public void addARecord(String domainName, String hostname, String ipv4) throws NetticaException {
        try {
            String query = "?U=" + username + "&P=" + password + "&FQDN=" + domainName + "&A=" + hostname + "&N=" + ipv4;
            URL url = new URL("http", "localhost", 80, "Domain/Update.aspx" + query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int response = conn.getResponseCode();
            if (response > 200) {
                throw new NetticaException(decodeStatus(response));
            }
        } catch (MalformedURLException ex) {
            throw new NetticaException(ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new NetticaException(ex.getMessage(), ex);
        }
    }
