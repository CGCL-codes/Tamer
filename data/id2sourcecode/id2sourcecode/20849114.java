        public ClientHttpResponse execute() throws IOException {
            byte[] bufferedOutput = bodyOutputStream.toByteArray();
            delegate.getBody().write(bufferedOutput);
            delegate.getHeaders().set("Authorization", oauth2Version.getAuthorizationHeaderValue(accessToken));
            return delegate.execute();
        }
