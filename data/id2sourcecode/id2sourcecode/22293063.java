    void loadFileContents() throws FrameworkException, ApplicationExceptions {
        Reader reader = null;
        try {
            URL url = URLHelper.newExtendedURL(m_validationRulesFile);
            File f = new File(url.getPath());
            m_fileUpdateable = f.exists() && f.isFile();
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buf = new StringBuffer();
            int i;
            while ((i = reader.read()) != -1) buf.append((char) i);
            m_fileContents = buf.toString();
            if (log.isDebugEnabled()) log.debug("Obtained contents from the file " + m_validationRulesFile);
            getUserSession().getWidgetCache(getComponentId()).removeModel("fileContents");
        } catch (MalformedURLException e) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new ConfigException(ConfigException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
            throw appExps;
        } catch (IOException e) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new ConfigException(ConfigException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
            throw appExps;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    String str = "Exception thrown while closing the Reader Stream";
                    log.error(str, e);
                    ApplicationExceptions appExps = new ApplicationExceptions();
                    appExps.add(new ConfigException(ConfigException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
                    throw appExps;
                }
            }
        }
    }
