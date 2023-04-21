        public Object get(URL url, String name, Purpose purpose) throws IOException {
            URLConnection conn = url.openConnection();
            String type = getContentType(conn, purpose);
            if (type == null) throw new UnknownServiceException("You must either load resource with content-type attribute or override DefaultResourceFactory.getContentType()");
            if (type.indexOf('.') < 0) return conn.getContent();
            return getContentHandler(type, conn, purpose).getContent(conn);
        }
