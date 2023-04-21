    public void parse(SmiMib mib, URL url, String resourceLocation) {
        InputStream is = null;
        try {
            m_log.debug("Parsing :" + url);
            is = url.openStream();
            is = new BufferedInputStream(is);
            SMILexer lexer = new SMILexer(is);
            SMIParser parser = new SMIParser(lexer);
            parser.init(mib, resourceLocation);
            SmiModule module = parser.module_definition();
            while (module != null) {
                module = parser.module_definition();
            }
        } catch (TokenStreamException e) {
            m_log.debug(e.getMessage(), e);
            m_reporter.reportTokenStreamError(resourceLocation, e.getMessage());
        } catch (RecognitionException e) {
            m_log.debug(e.getMessage(), e);
            m_reporter.reportParseError(new Location(resourceLocation, e.getLine(), e.getColumn()), e.getMessage());
        } catch (IOException e) {
            m_log.debug(e.getMessage(), e);
            m_reporter.reportIoException(new Location(resourceLocation, 0, 0), e.getMessage());
        } finally {
            m_log.debug("Finished parsing :" + resourceLocation);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    m_log.warn(e.getMessage(), e);
                }
            }
        }
    }
