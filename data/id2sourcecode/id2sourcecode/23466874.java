            public Properties run() {
                try {
                    InputStream stream = url.openStream();
                    if (stream != null) {
                        Properties props = new Properties();
                        props.load(stream);
                        stream.close();
                        return props;
                    }
                } catch (IOException e) {
                    if (isDiagnosticsEnabled()) {
                        logDiagnostic("Unable to read URL " + url);
                    }
                }
                return null;
            }
