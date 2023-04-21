    final void init(InputSource is, XMLScanner prologParser) throws IOException {
        postAction = null;
        iProlog = 0;
        this.prologParser = prologParser;
        elemDepth = 0;
        publicID = is.getPublicId();
        systemID = toURL(is.getSystemId());
        Reader charStream = is.getCharacterStream();
        if (charStream != null) setChannel(new NBReaderChannel(charStream)); else {
            ReadableByteChannel byteChannel = null;
            String encoding = is.getEncoding();
            if (is instanceof ChannelInputSource) {
                ChannelInputSource channelInputSource = (ChannelInputSource) is;
                byteChannel = channelInputSource.getChannel();
            }
            if (byteChannel == null) {
                InputStream inputStream = is.getByteStream();
                if (inputStream == null) {
                    assert systemID != null;
                    if (systemID.startsWith("file:/")) {
                        try {
                            inputStream = new FileInputStream(new File(new URI(systemID)));
                        } catch (URISyntaxException ex) {
                            throw new IOException(ex);
                        }
                    } else {
                        URLConnection con = new URL(systemID).openConnection();
                        if (con instanceof HttpURLConnection) {
                            final HttpURLConnection httpCon = (HttpURLConnection) con;
                            XMLEntityManager.setInstanceFollowRedirects(httpCon, true);
                        }
                        inputStream = con.getInputStream();
                        String contentType;
                        String charset = null;
                        String rawContentType = con.getContentType();
                        int index = (rawContentType != null) ? rawContentType.indexOf(';') : -1;
                        if (index != -1) {
                            contentType = rawContentType.substring(0, index).trim();
                            charset = rawContentType.substring(index + 1).trim();
                            if (charset.startsWith("charset=")) {
                                charset = charset.substring(8).trim();
                                if ((charset.charAt(0) == '"' && charset.charAt(charset.length() - 1) == '"') || (charset.charAt(0) == '\'' && charset.charAt(charset.length() - 1) == '\'')) {
                                    charset = charset.substring(1, charset.length() - 1);
                                }
                            }
                        } else contentType = rawContentType.trim();
                        String detectedEncoding = null;
                        if (contentType.equals("text/xml")) {
                            if (charset != null) detectedEncoding = charset; else detectedEncoding = "US-ASCII";
                        } else if (contentType.equals("application/xml")) {
                            if (charset != null) detectedEncoding = charset;
                        }
                        if (detectedEncoding != null) encoding = detectedEncoding;
                    }
                }
                byteChannel = new InputStreamChannel(inputStream);
            }
            nbChannel.setChannel(byteChannel);
            if (encoding == null) nbChannel.setEncoding("UTF-8", true); else nbChannel.setEncoding(encoding, false);
            setChannel(nbChannel);
        }
    }
