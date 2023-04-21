    public ExtensionHandlerGeneral(String namespaceUri, StringVector elemNames, StringVector funcNames, String scriptLang, String scriptSrcURL, String scriptSrc, String systemId) throws TransformerException {
        super(namespaceUri, scriptLang);
        if (elemNames != null) {
            Object junk = new Object();
            int n = elemNames.size();
            for (int i = 0; i < n; i++) {
                String tok = elemNames.elementAt(i);
                m_elements.put(tok, junk);
            }
        }
        if (funcNames != null) {
            Object junk = new Object();
            int n = funcNames.size();
            for (int i = 0; i < n; i++) {
                String tok = funcNames.elementAt(i);
                m_functions.put(tok, junk);
            }
        }
        m_scriptSrcURL = scriptSrcURL;
        m_scriptSrc = scriptSrc;
        if (m_scriptSrcURL != null) {
            URL url = null;
            try {
                url = new URL(m_scriptSrcURL);
            } catch (java.net.MalformedURLException mue) {
                int indexOfColon = m_scriptSrcURL.indexOf(':');
                int indexOfSlash = m_scriptSrcURL.indexOf('/');
                if ((indexOfColon != -1) && (indexOfSlash != -1) && (indexOfColon < indexOfSlash)) {
                    url = null;
                    throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_COULD_NOT_FIND_EXTERN_SCRIPT, new Object[] { m_scriptSrcURL }), mue);
                } else {
                    try {
                        url = new URL(new URL(SystemIDResolver.getAbsoluteURI(systemId)), m_scriptSrcURL);
                    } catch (java.net.MalformedURLException mue2) {
                        throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_COULD_NOT_FIND_EXTERN_SCRIPT, new Object[] { m_scriptSrcURL }), mue2);
                    }
                }
            }
            if (url != null) {
                try {
                    URLConnection uc = url.openConnection();
                    InputStream is = uc.getInputStream();
                    byte[] bArray = new byte[uc.getContentLength()];
                    is.read(bArray);
                    m_scriptSrc = new String(bArray);
                } catch (IOException ioe) {
                    throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_COULD_NOT_FIND_EXTERN_SCRIPT, new Object[] { m_scriptSrcURL }), ioe);
                }
            }
        }
        Object manager = null;
        try {
            manager = ObjectFactory.newInstance(BSF_MANAGER, ObjectFactory.findClassLoader(), true);
        } catch (ObjectFactory.ConfigurationError e) {
            e.printStackTrace();
        }
        if (manager == null) {
            throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_INIT_BSFMGR, null));
        }
        try {
            Method loadScriptingEngine = manager.getClass().getMethod("loadScriptingEngine", new Class[] { String.class });
            m_engine = loadScriptingEngine.invoke(manager, new Object[] { scriptLang });
            Method engineExec = m_engine.getClass().getMethod("exec", new Class[] { String.class, Integer.TYPE, Integer.TYPE, Object.class });
            engineExec.invoke(m_engine, new Object[] { "XalanScript", ZEROINT, ZEROINT, m_scriptSrc });
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransformerException(XSLMessages.createMessage(XSLTErrorResources.ER_CANNOT_CMPL_EXTENSN, null), e);
        }
    }
