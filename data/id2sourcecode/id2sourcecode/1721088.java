            protected URLConnection openConnection(URL _url) throws IOException {
                return new Connection(_url);
            }
