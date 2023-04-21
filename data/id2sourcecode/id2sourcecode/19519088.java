    Source generate(PtarURI uri) throws IOException, BuilderException, SAXException, TransformerException, MalformedURLException, InstantiationException, IllegalAccessException {
        if (log.isDebugEnabled()) log.debug("generate: uri=" + uri.toExternalForm());
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            if (uri.isFile()) {
                File file = uri.getFile();
                if (log.isDebugEnabled()) log.debug("generate: file=" + file);
                if (file == null || file.exists() == false) {
                    log.info("generate: file does not exist [" + file + "]");
                    return null;
                }
                is = new FileInputStream(file);
            } else {
                URL url = uri.getURL();
                if (log.isDebugEnabled()) log.debug("generate: url=" + url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "JReceiver/@version@");
                conn.setRequestProperty("Accept", "*/*");
                conn.setRequestProperty("Icy-MetaData", "1");
                conn.connect();
                is = conn.getInputStream();
            }
            InputSource input = new InputSource(is);
            input.setSystemId(uri.toExternalForm());
            if (m_generator == null) initGenerator();
            if (m_digester == null) initDigester();
            if (m_transformer == null) initTransformer();
            m_generator.resetData();
            m_generator.setContentHandler(m_source_filter);
            m_source_filter.setParent(m_generator);
            m_source_filter.resetData();
            SAXSource transform_source = new SAXSource(m_source_filter, input);
            if (log.isDebugEnabled()) log.debug("generate: executing pipeline");
            m_transformer.clearParameters();
            m_transformer.transform(transform_source, new SAXResult(m_digester));
            log.debug("generate: finished pipeline");
            Source source = (Source) m_digester.getRoot();
            if (log.isDebugEnabled()) log.debug("generate: source=" + source);
            return source;
        } finally {
            log.debug("closing");
            HelperFile.safeClose(is);
            log.debug("closed");
            if (conn != null) conn.disconnect();
        }
    }
