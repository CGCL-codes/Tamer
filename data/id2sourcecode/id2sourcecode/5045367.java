        private HttpURLConnection prepare(URL url, String method) {
            if (this.username != null && this.password != null) {
                this.headers.put("Authorization", "Basic " + Codec.encodeBASE64(this.username + ":" + this.password));
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                checkFileBody(connection);
                connection.setRequestMethod(method);
                for (String key : this.headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
                connection.setDoInput(true);
                if (this.oauthTokens != null) {
                    OAuthConsumer consumer = new DefaultOAuthConsumer(oauthInfo.consumerKey, oauthInfo.consumerSecret);
                    consumer.setTokenWithSecret(oauthTokens.token, oauthTokens.secret);
                    consumer.sign(connection);
                }
                return connection;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
