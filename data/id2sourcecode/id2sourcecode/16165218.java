    public void fullyParseGameData() throws GameParseException {
        m_data = null;
        InputStream input;
        String error = null;
        try {
            input = m_url.toURL().openStream();
            try {
                m_data = new GameParser().parse(input, false);
                m_gameDataFullyLoaded = true;
            } catch (final EngineVersionException e) {
                System.out.println(e.getMessage());
                error = e.getMessage();
            } catch (final SAXParseException e) {
                System.err.println("Could not parse:" + m_url + " error at line:" + e.getLineNumber() + " column:" + e.getColumnNumber());
                e.printStackTrace();
                error = e.getMessage();
            } catch (final Exception e) {
                System.err.println("Could not parse:" + m_url);
                e.printStackTrace();
                error = e.getMessage();
            } finally {
                try {
                    input.close();
                } catch (final IOException e) {
                }
            }
        } catch (final MalformedURLException e1) {
            e1.printStackTrace();
            error = e1.getMessage();
        } catch (final IOException e1) {
            e1.printStackTrace();
            error = e1.getMessage();
        }
        if (error != null) throw new GameParseException(error);
    }
