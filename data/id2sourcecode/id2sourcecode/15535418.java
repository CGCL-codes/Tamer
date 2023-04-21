            @Override
            public InputStream getStream() {
                try {
                    return url.openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
