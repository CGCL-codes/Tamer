    protected Reader getReader(XMLInputSource source) throws IOException {
        if (source.getCharacterStream() != null) {
            return source.getCharacterStream();
        } else {
            InputStream stream = null;
            String encoding = source.getEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            if (source.getByteStream() != null) {
                stream = source.getByteStream();
                if (!(stream instanceof BufferedInputStream)) {
                    stream = new BufferedInputStream(stream, fTempString.ch.length);
                }
            } else {
                String expandedSystemId = XMLEntityManager.expandSystemId(source.getSystemId(), source.getBaseSystemId(), false);
                URL url = new URL(expandedSystemId);
                URLConnection urlCon = url.openConnection();
                if (urlCon instanceof HttpURLConnection && source instanceof HTTPInputSource) {
                    final HttpURLConnection urlConnection = (HttpURLConnection) urlCon;
                    final HTTPInputSource httpInputSource = (HTTPInputSource) source;
                    Iterator propIter = httpInputSource.getHTTPRequestProperties();
                    while (propIter.hasNext()) {
                        Map.Entry entry = (Map.Entry) propIter.next();
                        urlConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    }
                    boolean followRedirects = httpInputSource.getFollowHTTPRedirects();
                    if (!followRedirects) {
                        XMLEntityManager.setInstanceFollowRedirects(urlConnection, followRedirects);
                    }
                }
                stream = new BufferedInputStream(urlCon.getInputStream());
                String rawContentType = urlCon.getContentType();
                int index = (rawContentType != null) ? rawContentType.indexOf(';') : -1;
                String contentType = null;
                String charset = null;
                if (index != -1) {
                    contentType = rawContentType.substring(0, index).trim();
                    charset = rawContentType.substring(index + 1).trim();
                    if (charset.startsWith("charset=")) {
                        charset = charset.substring(8).trim();
                        if ((charset.charAt(0) == '"' && charset.charAt(charset.length() - 1) == '"') || (charset.charAt(0) == '\'' && charset.charAt(charset.length() - 1) == '\'')) {
                            charset = charset.substring(1, charset.length() - 1);
                        }
                    } else {
                        charset = null;
                    }
                } else {
                    contentType = rawContentType.trim();
                }
                String detectedEncoding = null;
                if (contentType.equals("text/xml")) {
                    if (charset != null) {
                        detectedEncoding = charset;
                    } else {
                        detectedEncoding = "US-ASCII";
                    }
                } else if (contentType.equals("application/xml")) {
                    if (charset != null) {
                        detectedEncoding = charset;
                    } else {
                        detectedEncoding = getEncodingName(stream);
                    }
                } else if (contentType.endsWith("+xml")) {
                    detectedEncoding = getEncodingName(stream);
                }
                if (detectedEncoding != null) {
                    encoding = detectedEncoding;
                }
            }
            encoding = encoding.toUpperCase(Locale.ENGLISH);
            encoding = consumeBOM(stream, encoding);
            if (encoding.equals("UTF-8")) {
                return new UTF8Reader(stream, fTempString.ch.length, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
            }
            String javaEncoding = EncodingMap.getIANA2JavaMapping(encoding);
            if (javaEncoding == null) {
                MessageFormatter aFormatter = fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN);
                Locale aLocale = fErrorReporter.getLocale();
                throw new IOException(aFormatter.formatMessage(aLocale, "EncodingDeclInvalid", new Object[] { encoding }));
            } else if (javaEncoding.equals("ASCII")) {
                return new ASCIIReader(stream, fTempString.ch.length, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
            } else if (javaEncoding.equals("ISO8859_1")) {
                return new Latin1Reader(stream, fTempString.ch.length);
            }
            return new InputStreamReader(stream, javaEncoding);
        }
    }
