        @Override
        public TypedStream open(String uri) {
            int lastSlash = uri.lastIndexOf('/');
            if (uri.lastIndexOf('.') <= lastSlash) uri = uri + ".rdf";
            try {
                URL url = new URL(uri);
                ResourceLocator.this.logger.debug("Load " + uri);
                return new TypedStream(url.openStream());
            } catch (Exception ex) {
                return null;
            }
        }
