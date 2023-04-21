        @Override
        protected URLConnection openConnection(URL url) throws IOException {
            return new URLConnectionImpl(url, _delegate.openConnection(), this);
        }
