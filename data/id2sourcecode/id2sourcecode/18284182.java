    private HttpURLConnection getConnection() {
        String urlString = PreferenceQuery.getAddress() + ":" + PreferenceQuery.getPort() + this.svrsQuery;
        System.out.println("urlstring: " + urlString);
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (PreferenceQuery.getPassword() != null && PreferenceQuery.getUsername() != null) {
                String auth = PreferenceQuery.getUsername() + ":" + PreferenceQuery.getPassword();
                String encodedAuth = Base64Coder.encodeString(auth);
                connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            }
            if (connection == null) {
                errors.add("unknown error creating SSL socket.");
                return null;
            }
            connection.setRequestProperty("User-Agent", "SeaMonster/");
            connection.setConnectTimeout((int) (PreferenceQuery.getConnectTimeout() * 1000.0));
            connection.setReadTimeout((int) (PreferenceQuery.getReadTimout() * 1000.0));
            connection.setInstanceFollowRedirects(true);
            return connection;
        } catch (MalformedURLException e) {
            errors.add("malformed URL");
            return null;
        } catch (IOException e) {
            errors.add("I/O error");
            return null;
        }
    }
