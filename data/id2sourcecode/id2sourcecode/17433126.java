        protected URLConnection openConnection(URL u) throws IOException {
            URL httpurl;
            if (u.getPort() != -1) httpurl = new URL("http", u.getHost(), u.getPort(), u.getFile()); else httpurl = new URL("http", u.getHost(), u.getFile());
            System.out.println("Transformed " + u.getProtocol() + " into http");
            return httpurl.openConnection();
        }
