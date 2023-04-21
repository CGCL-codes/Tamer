    public HandleCheckResult checkHandle(String handle, HandleProvider provider) throws HandleCheckException {
        LOG.info("Checking handle '" + handle + "' for provider '" + provider + "'");
        boolean available;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(provider.getProviderUri(URLEncoder.encode(handle, "UTF-8")));
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.connect();
            final int code = conn.getResponseCode();
            final String contentBody = readContentBody(conn);
            available = provider.determineAvailability(handle, code, contentBody);
            LOG.info("Received HTTP response code " + code + " from " + url);
            final String message = conn.getResponseMessage();
            persistRequestInfo(handle, provider, available, url, code, message);
        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE, "Error for handle '" + handle + "' for provider '" + provider + "'", e);
            throw new HandleCheckException(e, handle, provider);
        } catch (FileNotFoundException e) {
            LOG.info(e.getMessage());
            available = true;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error for handle '" + handle + "' for provider '" + provider + "'", e);
            throw new HandleCheckException(e, handle, provider);
        } catch (Throwable t) {
            LOG.log(Level.SEVERE, "Error for handle '" + handle + "' for provider '" + provider + "'", t);
            throw new HandleCheckException(t, handle, provider);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return new HandleCheckResult(handle, provider, available);
    }
