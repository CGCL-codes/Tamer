    public void initialiseManager() {
        CharsetDigesterDriver dd = new CharsetDigesterDriver();
        Charsets css = dd.digest();
        createdEncodingMap = new HashMap();
        charsetNameAliasMap = new HashMap();
        preloadedEncodingMap = new HashMap();
        unsupportedCharsetNameSet = new HashSet();
        charsetMap = new HashMap();
        if (css != null) {
            ArrayList charsets = css.getCharsets();
            Iterator i = charsets.iterator();
            while (i.hasNext()) {
                Charset cs = (Charset) i.next();
                String name = cs.getName();
                this.charsetMap.put(name, cs);
                ArrayList aliases = cs.getAlias();
                if (aliases != null) {
                    Iterator aliasIterator = aliases.iterator();
                    while (aliasIterator.hasNext()) {
                        Alias a = (Alias) aliasIterator.next();
                        charsetNameAliasMap.put(a.getName(), name);
                    }
                }
                if (cs.isPreload()) {
                    try {
                        preloadedEncodingMap.put(name, createEncoding(cs));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Loaded '" + cs + "'");
                }
            }
        }
    }
