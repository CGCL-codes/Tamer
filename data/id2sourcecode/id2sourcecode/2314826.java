            protected URLConnection openConnection(URL _url) throws IOException {
                System.err.println("FFR: openConnection(" + _url + ")");
                return new Connection(_url);
            }
