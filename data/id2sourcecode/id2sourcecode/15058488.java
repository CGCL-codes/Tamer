    private void addResponse() {
        try {
            long before = System.currentTimeMillis();
            URLConnection urlConnection = new URL(ConnectionSpeedProperties.URL).openConnection();
            urlConnection.connect();
            logger.debug(urlConnection.getHeaderFields().get(null).get(0));
            if (this.responses.size() >= ConnectionSpeedProperties.ENTRIES) {
                this.responses.remove(0);
            }
            this.responses.add(System.currentTimeMillis() - before);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
