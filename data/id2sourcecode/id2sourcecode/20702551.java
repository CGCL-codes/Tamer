    private SAXParseException readText(URL url, String href, String encoding) throws SAXException {
        InputStream in = null;
        try {
            URLConnection conn;
            InputStreamReader reader;
            char buf[] = new char[4096];
            int count;
            url = new URL(url, href);
            conn = url.openConnection();
            in = conn.getInputStream();
            if (encoding == null) encoding = Resolver.getEncoding(conn.getContentType());
            if (encoding == null) {
                ErrorHandler eh = getErrorHandler();
                if (eh != null) eh.warning(new SAXParseException("guessing text encoding for URL: " + url, locator));
                reader = new InputStreamReader(in);
            } else reader = new InputStreamReader(in, encoding);
            while ((count = reader.read(buf, 0, buf.length)) != -1) super.characters(buf, 0, count);
            in.close();
            return null;
        } catch (IOException e) {
            return new SAXParseException("can't XInclude text", locator, e);
        }
    }
