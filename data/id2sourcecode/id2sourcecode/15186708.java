        public ClientHttpResponse execute() throws IOException {
            byte[] bufferedOutput = bodyOutputStream.toByteArray();
            String authorizationHeader = OAuth1Utils.spring30buildAuthorizationHeaderValue(this, bufferedOutput, consumerKey, consumerSecret, accessToken, accessTokenSecret);
            delegate.getBody().write(bufferedOutput);
            delegate.getHeaders().set("Authorization", authorizationHeader);
            return delegate.execute();
        }
