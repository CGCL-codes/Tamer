    @SuppressWarnings({ "ResultOfMethodCallIgnored" })
    protected void buildWritePaths(org.w3c.dom.Node dataFileCacheNode) {
        javax.xml.xpath.XPathFactory pathFactory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath pathFinder = pathFactory.newXPath();
        try {
            org.w3c.dom.NodeList locationNodes = (org.w3c.dom.NodeList) pathFinder.evaluate("/dataFileStore/writeLocations/location", dataFileCacheNode.getFirstChild(), javax.xml.xpath.XPathConstants.NODESET);
            for (int i = 0; i < locationNodes.getLength(); i++) {
                org.w3c.dom.Node location = locationNodes.item(i);
                String prop = pathFinder.evaluate("@property", location);
                String wwDir = pathFinder.evaluate("@wwDir", location);
                String append = pathFinder.evaluate("@append", location);
                String create = pathFinder.evaluate("@create", location);
                String path = buildLocationPath(prop, append, wwDir);
                if (path == null) {
                    Logging.logger().log(Level.WARNING, "FileStore.LocationInvalid", prop != null ? prop : Logging.getMessage("generic.Unknown"));
                    continue;
                }
                Logging.logger().log(Level.FINER, "FileStore.AttemptingWriteDir", path);
                java.io.File pathFile = new java.io.File(path);
                if (!pathFile.exists() && create != null && (create.contains("t") || create.contains("T"))) {
                    Logging.logger().log(Level.FINER, "FileStore.MakingDirsFor", path);
                    pathFile.mkdirs();
                }
                if (pathFile.isDirectory() && pathFile.canWrite() && pathFile.canRead()) {
                    Logging.logger().log(Level.FINER, "FileStore.WriteLocationSuccessful", path);
                    this.writeLocation = new StoreLocation(pathFile);
                    StoreLocation oldLocation = this.storeLocationFor(path);
                    if (oldLocation != null) this.readLocations.remove(oldLocation);
                    this.readLocations.add(0, this.writeLocation);
                    break;
                }
            }
        } catch (javax.xml.xpath.XPathExpressionException e) {
            String message = Logging.getMessage("FileStore.ExceptionReadingConfigurationFile");
            Logging.logger().severe(message);
            throw new IllegalStateException(message, e);
        }
    }
