            public Object run() {
                Properties props = null;
                try {
                    InputStream stream;
                    stream = url.openStream();
                    props = new Properties();
                    props.load(stream);
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return props;
            }
