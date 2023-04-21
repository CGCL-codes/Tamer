        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            MerlotDebug.msg("Resolving entity: publicId: " + publicId + " systemId: " + systemId);
            this.publicId = publicId;
            this.systemId = systemId;
            DTDCacheEntry dtdentry = null;
            if (systemId.startsWith("plugin:")) {
                String path = systemId.substring(10);
                PluginClassLoader pcl;
                PluginManager pm = PluginManager.getInstance();
                List<PluginConfig> plugins = pm.getPlugins();
                URL url = null;
                File tryPlugin;
                for (int i = 0; i < plugins.size(); i++) {
                    tryPlugin = ((PluginConfig) plugins.get(i)).getSource();
                    pcl = new PluginClassLoader(tryPlugin);
                    url = pcl.findResource(path);
                    if (url != null) {
                        break;
                    }
                }
                if (url != null) {
                    InputSource src = new InputSource(url.openStream());
                    return src;
                }
            }
            dtdentry = DTDCache.getSharedInstance().findDTD(publicId, systemId, _vdoc.getFileLocation());
            if (dtdentry != null) {
                _vdoc.addDTD(dtdentry, systemId);
                InputStream is = null;
                PluginClassLoader pcl;
                PluginManager pm = PluginManager.getInstance();
                List<PluginConfig> plugins = pm.getPlugins();
                URL url = null;
                File tryPlugin;
                for (int i = 0; i < plugins.size(); i++) {
                    tryPlugin = ((PluginConfig) plugins.get(i)).getSource();
                    pcl = new PluginClassLoader(tryPlugin);
                    url = pcl.findResource(dtdentry.getFilePath());
                    if (url != null) {
                        break;
                    }
                }
                if (url != null) {
                    try {
                        is = url.openStream();
                        InputSource src = new InputSource(is);
                        src.setSystemId("plugin:///" + dtdentry.getFilePath());
                        return src;
                    } catch (IOException ioe) {
                    }
                }
                try {
                    String path = dtdentry.getFilePath();
                    if (path != null && path.startsWith("file:")) {
                        path = path.substring(5);
                        is = new FileInputStream(path);
                    } else if (systemId != null && systemId.startsWith("http:")) {
                        url = new URL(systemId);
                        is = url.openStream();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    TolvenLogger.info("plugin uri is probably invalid", DOMLiaison.class);
                    return null;
                }
                if (is != null) {
                    InputSource src = new InputSource(is);
                    src.setSystemId(systemId);
                    return src;
                }
                char[] chars = dtdentry.getCachedDTDStream();
                CharArrayReader r = new CharArrayReader(chars);
                return new InputSource(r);
            }
            if (_entityResolverList != null) {
                if (dtdentry == null) {
                    for (int i = _entityResolverList.size() - 1; i >= 0; i--) {
                        EntityResolver er = _entityResolverList.elementAt(i);
                        try {
                            dtdentry = DTDCache.getSharedInstance().resolveDTD(publicId, systemId, er, _vdoc.getFileLocation());
                        } catch (IOException ioe) {
                            continue;
                        }
                        if (dtdentry != null) {
                            CharArrayReader car = new CharArrayReader(dtdentry.getCachedDTDStream());
                            _vdoc.addDTD(dtdentry, systemId);
                            return new InputSource(car);
                        }
                    }
                }
            }
            TolvenLogger.info("Resolve entity is returning null.", DOMLiaison.class);
            return null;
        }
