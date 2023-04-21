    protected void obtainDefaultValues() throws FrameworkException {
        Reader reader = null;
        try {
            if (m_defaultValueFile != null && m_defaultValues == null) {
                URL url = new URL(m_defaultValueFile);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer buf = new StringBuffer();
                int i;
                while ((i = reader.read()) != -1) buf.append((char) i);
                m_defaultValues = buf.toString();
                if (log.isDebugEnabled()) log.debug("Obtained contents from the default value file " + m_defaultValueFile);
                File f = new File(url.getPath());
                m_fileUpdateable = f.exists() && f.isFile();
                getUserSession().getWidgetCache(getComponentId()).removeModel("defaultValues");
            } else {
                if (log.isDebugEnabled()) log.debug("Contents have already been obtained from the default value file " + m_defaultValueFile);
            }
        } catch (IOException e) {
            String str = "Exception thrown while reading the default value file: " + m_defaultValueFile;
            log.error(str, e);
            throw new DefaultValueEditorException(DefaultValueEditorException.READ_FAILED, new Object[] { m_defaultValueFile }, e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
            }
        }
    }
