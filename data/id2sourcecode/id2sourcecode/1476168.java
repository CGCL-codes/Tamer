    private ClassMetaData parseNew(String classname) {
        if (log.isDebugEnabled()) {
            log.debug("Locating the mapping file for the Persistent class " + classname);
        }
        String shortname = classname.substring(classname.lastIndexOf('.') + 1);
        for (Iterator itr = m_locations.iterator(); itr.hasNext(); ) {
            DirLoc dirLoc = (DirLoc) itr.next();
            String packageName = dirLoc.getDir();
            StringBuffer urlNameBuf = new StringBuffer(packageName);
            if (!packageName.endsWith("/") && !packageName.endsWith("\\")) {
                urlNameBuf.append('/');
            }
            urlNameBuf.append(shortname).append(MAPPING_FILE_PREFIX);
            String urlName = urlNameBuf.toString();
            if (log.isDebugEnabled()) {
                log.debug("Looking for the mapping file " + urlName);
            }
            URL url = null;
            try {
                url = URLHelper.newExtendedURL(urlName);
            } catch (MalformedURLException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Could not find the mapping file " + urlName + ". " + e.getMessage());
                }
            }
            if (url != null) {
                DefaultHandler handler = new MappingParser();
                InputStream stream = null;
                try {
                    XMLReader reader = XMLReaderFactory.createXMLReader(PARSER_NAME);
                    reader.setContentHandler(handler);
                    reader.setEntityResolver(new DefaultEntityResolver());
                    reader.setErrorHandler(new DefaultErrorHandler());
                    stream = url.openStream();
                    reader.parse(new InputSource(stream));
                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Error in parsing the mapping file " + urlName + ". Will try and look at another location", e);
                    }
                    continue;
                } finally {
                    try {
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (IOException e) {
                    }
                }
                ClassMetaData classMetaData = ((MappingParser) handler).getMetaData();
                if (classMetaData != null && classMetaData.getClassName().equals(classname)) {
                    if (log.isDebugEnabled()) {
                        log.debug("Validating the ClassMetaData object for the mapping file " + urlName);
                    }
                    classMetaData.setXmlFileUrl(url);
                    classMetaData.validate();
                    synchronized (m_metaCache) {
                        if (m_metaCache.containsKey(classname)) {
                            classMetaData = (ClassMetaData) m_metaCache.get(classname);
                        } else {
                            m_metaCache.put(classname, classMetaData);
                        }
                    }
                    return classMetaData;
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("The classname in the mapping file " + urlName + ", does not match the required value " + classname + ". Will try and look at another location");
                    }
                }
            }
        }
        String str = "Could not find/parse the mapping file for the class " + classname;
        log.error(str);
        throw new ConfigurationServiceRuntimeException(str);
    }
