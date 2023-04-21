    @Override
    public XmlCollectionSet collect(CollectionAgent agent, XmlDataCollection collection, Map<String, Object> parameters) throws CollectionException {
        XmlCollectionSet collectionSet = new XmlCollectionSet(agent);
        collectionSet.setCollectionTimestamp(new Date());
        collectionSet.setStatus(ServiceCollector.COLLECTION_UNKNOWN);
        try {
            File resourceDir = new File(getRrdRepository().getRrdBaseDir(), Integer.toString(agent.getNodeId()));
            for (XmlSource source : collection.getXmlSources()) {
                if (!source.getUrl().startsWith(Sftp3gppUrlHandler.PROTOCOL)) {
                    throw new CollectionException("The 3GPP SFTP Collection Handler can only use the protocol " + Sftp3gppUrlHandler.PROTOCOL);
                }
                String urlStr = parseUrl(source.getUrl(), agent, collection.getXmlRrd().getStep());
                URL url = UrlFactory.getUrl(urlStr);
                String lastFile = getLastFilename(resourceDir, url.getPath());
                Sftp3gppUrlConnection connection = (Sftp3gppUrlConnection) url.openConnection();
                if (lastFile == null) {
                    lastFile = connection.get3gppFileName();
                    log().debug("collect(single): retrieving file from " + url.getPath() + File.separatorChar + lastFile + " from " + agent.getHostAddress());
                    Document doc = getXmlDocument(urlStr);
                    fillCollectionSet(agent, collectionSet, source, doc);
                    setLastFilename(resourceDir, url.getPath(), lastFile);
                    deleteFile(connection, lastFile);
                } else {
                    connection.connect();
                    List<String> files = connection.getFileList();
                    long lastTs = connection.getTimeStampFromFile(lastFile);
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    factory.setIgnoringComments(true);
                    boolean collected = false;
                    for (String fileName : files) {
                        if (connection.getTimeStampFromFile(fileName) > lastTs) {
                            log().debug("collect(multiple): retrieving file " + fileName + " from " + agent.getHostAddress());
                            InputStream is = connection.getFile(fileName);
                            Document doc = builder.parse(is);
                            fillCollectionSet(agent, collectionSet, source, doc);
                            setLastFilename(resourceDir, url.getPath(), fileName);
                            deleteFile(connection, fileName);
                            collected = true;
                        }
                    }
                    if (!collected) {
                        log().warn("collect: could not find any file after " + lastFile + " on " + agent);
                    }
                    connection.disconnect();
                }
            }
            collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);
            return collectionSet;
        } catch (Exception e) {
            collectionSet.setStatus(ServiceCollector.COLLECTION_FAILED);
            throw new CollectionException(e.getMessage(), e);
        }
    }
