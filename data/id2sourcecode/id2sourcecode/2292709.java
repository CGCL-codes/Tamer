    public static RDFFormat read(final URL url, final SesameInputAdapter sa, final String baseUri, RDFFormat format) throws RippleException {
        String urlStr = url.toString();
        if (urlStr.startsWith("jar:")) {
            if (null == format) {
                format = RDFUtils.guessRdfFormat(urlStr, null);
            }
            JarURLConnection jc;
            InputStream is;
            try {
                jc = (JarURLConnection) url.openConnection();
                is = jc.getInputStream();
            } catch (IOException e) {
                throw new RippleException(e);
            }
            RDFUtils.read(is, sa, baseUri, format);
            try {
                is.close();
            } catch (IOException e) {
                throw new RippleException(e);
            }
            return format;
        } else if (urlStr.startsWith("file:")) {
            if (null == format) {
                format = RDFUtils.guessRdfFormat(urlStr, null);
            }
            InputStream is;
            try {
                is = new FileInputStream(urlStr.substring(5));
            } catch (IOException e) {
                throw new RippleException(e);
            }
            RDFUtils.read(is, sa, baseUri, format);
            try {
                is.close();
            } catch (IOException e) {
                throw new RippleException(e);
            }
            return format;
        } else {
            HttpMethod method = HTTPUtils.createRdfGetMethod(url.toString());
            return read(method, sa, baseUri, format);
        }
    }
