    public void delayParseGameData() {
        m_data = null;
        InputStream input;
        try {
            input = m_url.toURL().openStream();
            try {
                m_data = new GameParser().parse(input, true);
                m_gameDataFullyLoaded = false;
            } catch (final EngineVersionException e) {
                System.out.println(e.getMessage());
            } catch (final SAXParseException e) {
                System.err.println("Could not parse:" + m_url + " error at line:" + e.getLineNumber() + " column:" + e.getColumnNumber());
                e.printStackTrace();
            } catch (final Exception e) {
                System.err.println("Could not parse:" + m_url);
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (final IOException e) {
                }
            }
        } catch (final MalformedURLException e1) {
            e1.printStackTrace();
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
    }
