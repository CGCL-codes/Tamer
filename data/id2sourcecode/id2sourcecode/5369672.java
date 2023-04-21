        @Override
        public InputStream convert(Object o) {
            if (o == null) {
                return null;
            }
            if (o instanceof InputStream) {
                return (InputStream) o;
            }
            try {
                if (o instanceof File) {
                    return new FileInputStream((File) o);
                }
                URL url = URL_CONVERTER.convert(o);
                if (url != null) {
                    return url.openStream();
                }
            } catch (IOException ex) {
                LOG.log(Level.CONFIG, ex.getMessage(), ex);
            }
            return null;
        }
