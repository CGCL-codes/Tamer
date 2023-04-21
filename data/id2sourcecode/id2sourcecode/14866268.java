            public Object run() {
                URL url = findResource(n);
                try {
                    return url != null ? url.openStream() : null;
                } catch (IOException e) {
                    return null;
                }
            }
