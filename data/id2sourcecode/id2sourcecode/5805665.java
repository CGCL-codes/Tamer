        private void _testForSSL() {
            HttpURLConnection conn = null;
            try {
                _serverSelected.setScheme(Server.Scheme.HTTP);
                URL url = new URL(_serverSelected.getURIString());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("HEAD");
                int responseCode = conn.getResponseCode();
                if (responseCode > 0 && responseCode < 400) dropContent(conn.getInputStream()); else if (responseCode >= 400) dropContent(conn.getErrorStream()); else throw new Exception("Wrong HTTP response code");
            } catch (Exception ex) {
                if (conn != null) conn.disconnect();
                _serverSelected.setScheme(Server.Scheme.HTTPS);
            }
        }
