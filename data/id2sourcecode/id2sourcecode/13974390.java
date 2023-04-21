    public Reader getURL(String streamName) throws TokenizerParseException {
        try {
            InputStream scriptStream = null;
            if (m_packagerManager != null) {
                for (int i = 0; i < m_scriptPath.length; i++) {
                    try {
                        scriptStream = m_packagerManager.getByteStream(m_scriptPath[i] + streamName + ".script");
                        if (scriptStream != null) {
                            InputStreamReader reader = new InputStreamReader(scriptStream);
                            return reader;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
            for (int i = 0; i < m_scriptPath.length; i++) {
                try {
                    URL url = new URL(m_documentBase, m_scriptPath[i] + streamName + ".script");
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setDoOutput(false);
                    urlConnection.setDefaultUseCaches(true);
                    urlConnection.setUseCaches(true);
                    urlConnection.connect();
                    scriptStream = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(scriptStream);
                    return reader;
                } catch (Exception e) {
                    continue;
                }
            }
            throw new TokenizerParseException("Could not find script in the URL path(s) specified: " + streamName);
        } catch (TokenizerParseException e) {
            throw e;
        } catch (Exception e) {
            throw new TokenizerParseException("Unable to find script in the URL path(s) specified. Name: " + streamName + " Underlying Exception: " + e);
        }
    }
