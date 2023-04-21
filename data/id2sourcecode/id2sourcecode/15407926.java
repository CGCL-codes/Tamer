    private static List<SkinsNode> _getMetaInfSkinsNodeList() {
        List<SkinsNode> allSkinsNodes = new ArrayList<SkinsNode>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(_META_INF_CONFIG_FILE);
            Set<String> urlPaths = new HashSet<String>(16);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                boolean successfullyAdded = urlPaths.add(url.getPath());
                if (!successfullyAdded) {
                    if (_LOG.isFinest()) {
                        _LOG.finest("Skipping skin URL:{0} because it was already processed. " + "It was on the classpath more than once.", url);
                    }
                } else {
                    _LOG.finest("Processing skin URL:{0}", url);
                    InputStream in = url.openStream();
                    try {
                        if (in != null) {
                            SkinsNode metaInfSkinsNode = _getSkinsNodeFromInputStream(null, null, in, _getDefaultManager(), _META_INF_CONFIG_FILE);
                            allSkinsNodes.add(metaInfSkinsNode);
                        }
                    } catch (Exception e) {
                        _LOG.warning("ERR_PARSING", url);
                        _LOG.warning(e);
                    } finally {
                        in.close();
                    }
                }
            }
        } catch (IOException e) {
            _LOG.severe("ERR_LOADING_FILE", _META_INF_CONFIG_FILE);
            _LOG.severe(e);
        }
        return allSkinsNodes;
    }
