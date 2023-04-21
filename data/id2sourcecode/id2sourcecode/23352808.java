    public static synchronized void parseSDFSConfigFile(String fileName) throws Exception {
        File file = new File(fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        String version = "0.8.12";
        if (doc.getDocumentElement().hasAttribute("version")) {
            version = doc.getDocumentElement().getAttribute("version");
            Main.version = version;
        }
        SDFSLogger.getLog().info("Running SDFS Version " + Main.version);
        Main.version = version;
        SDFSLogger.getLog().info("Parsing " + doc.getDocumentElement().getNodeName() + " version " + version);
        Element locations = (Element) doc.getElementsByTagName("locations").item(0);
        SDFSLogger.getLog().info("parsing folder locations");
        Main.dedupDBStore = locations.getAttribute("dedup-db-store");
        Main.ioLogFile = locations.getAttribute("io-log.log");
        Element cache = (Element) doc.getElementsByTagName("io").item(0);
        if (cache.hasAttribute("log-level")) {
            SDFSLogger.setLevel(Integer.parseInt(cache.getAttribute("log-level")));
        }
        Main.safeClose = Boolean.parseBoolean(cache.getAttribute("safe-close"));
        Main.safeSync = Boolean.parseBoolean(cache.getAttribute("safe-sync"));
        Main.writeThreads = Integer.parseInt(cache.getAttribute("write-threads"));
        if (cache.hasAttribute("hash-size")) {
            Main.hashLength = Short.parseShort(cache.getAttribute("hash-size"));
            SDFSLogger.getLog().info("Setting hash size to " + Main.hashLength);
        }
        Main.dedupFiles = Boolean.parseBoolean(cache.getAttribute("dedup-files"));
        Main.CHUNK_LENGTH = Integer.parseInt(cache.getAttribute("chunk-size")) * 1024;
        Main.multiReadTimeout = Integer.parseInt(cache.getAttribute("multi-read-timeout"));
        Main.blankHash = new byte[Main.CHUNK_LENGTH];
        Main.systemReadCacheSize = Integer.parseInt(cache.getAttribute("system-read-cache"));
        Main.maxWriteBuffers = Integer.parseInt(cache.getAttribute("max-file-write-buffers"));
        Main.maxOpenFiles = Integer.parseInt(cache.getAttribute("max-open-files"));
        Main.maxInactiveFileTime = Integer.parseInt(cache.getAttribute("max-file-inactive")) * 1000;
        Main.fDkiskSchedule = cache.getAttribute("claim-hash-schedule");
        Element volume = (Element) doc.getElementsByTagName("volume").item(0);
        Main.volume = new Volume(volume);
        Element permissions = (Element) doc.getElementsByTagName("permissions").item(0);
        Main.defaultGroup = Integer.parseInt(permissions.getAttribute("default-group"));
        Main.defaultOwner = Integer.parseInt(permissions.getAttribute("default-owner"));
        Main.defaultFilePermissions = Integer.parseInt(permissions.getAttribute("default-file"));
        Main.defaultDirPermissions = Integer.parseInt(permissions.getAttribute("default-folder"));
        Main.chunkStorePageSize = Main.CHUNK_LENGTH;
        SDFSLogger.getLog().debug("parsing local chunkstore parameters");
        Element localChunkStore = (Element) doc.getElementsByTagName("local-chunkstore").item(0);
        Main.chunkStoreLocal = Boolean.parseBoolean(localChunkStore.getAttribute("enabled"));
        if (Main.chunkStoreLocal) {
            SDFSLogger.getLog().debug("this is a local chunkstore");
            Main.chunkStore = localChunkStore.getAttribute("chunk-store");
            if (localChunkStore.hasAttribute("gc-name")) Main.gcClass = localChunkStore.getAttribute("gc-name");
            Main.chunkStoreAllocationSize = Long.parseLong(localChunkStore.getAttribute("allocation-size"));
            Main.gcChunksSchedule = localChunkStore.getAttribute("chunk-gc-schedule");
            Main.chunkStoreClass = "org.opendedup.sdfs.filestore.FileChunkStore";
            if (localChunkStore.hasAttribute("chunkstore-class")) Main.chunkStoreClass = localChunkStore.getAttribute("chunkstore-class");
            if (localChunkStore.hasAttribute("hashdb-class")) Main.hashesDBClass = localChunkStore.getAttribute("hashdb-class");
            if (localChunkStore.getElementsByTagName("extended-config").getLength() > 0) {
                Main.chunkStoreConfig = (Element) localChunkStore.getElementsByTagName("extended-config").item(0);
            }
            Main.evictionAge = Integer.parseInt(localChunkStore.getAttribute("eviction-age"));
            if (localChunkStore.hasAttribute("encrypt")) {
                Main.chunkStoreEncryptionEnabled = Boolean.parseBoolean("encrypt");
                Main.chunkStoreEncryptionKey = localChunkStore.getAttribute("encryption-key");
            }
            Main.hashDBStore = localChunkStore.getAttribute("hash-db-store");
            Main.preAllocateChunkStore = Boolean.parseBoolean(localChunkStore.getAttribute("pre-allocate"));
            Main.chunkStoreReadAheadPages = Integer.parseInt(localChunkStore.getAttribute("read-ahead-pages"));
            Element networkcs = (Element) doc.getElementsByTagName("network").item(0);
            if (networkcs != null) {
                Main.enableNetworkChunkStore = Boolean.parseBoolean(networkcs.getAttribute("enable"));
                Main.serverHostName = networkcs.getAttribute("hostname");
                Main.useUDP = Boolean.parseBoolean(networkcs.getAttribute("use-udp"));
                Main.serverPort = Integer.parseInt(networkcs.getAttribute("port"));
                if (networkcs.hasAttribute("upstream-enabled")) {
                    Main.upStreamDSEHostEnabled = Boolean.parseBoolean(networkcs.getAttribute("upstream-enabled"));
                    Main.upStreamDSEHostName = networkcs.getAttribute("upstream-host");
                    Main.upStreamDSEPort = Integer.parseInt(networkcs.getAttribute("upstream-host-port"));
                    Main.upStreamPassword = networkcs.getAttribute("upstream-password");
                }
            }
            SDFSLogger.getLog().info("######### Will allocate " + Main.chunkStoreAllocationSize + " in chunkstore ##############");
            int awsSz = localChunkStore.getElementsByTagName("aws").getLength();
            if (awsSz > 0) {
                Main.chunkStoreClass = "org.opendedup.sdfs.filestore.S3ChunkStore";
                Element aws = (Element) localChunkStore.getElementsByTagName("aws").item(0);
                Main.AWSChunkStore = Boolean.parseBoolean(aws.getAttribute("enabled"));
                Main.awsAccessKey = aws.getAttribute("aws-access-key");
                Main.awsSecretKey = aws.getAttribute("aws-secret-key");
                Main.awsBucket = aws.getAttribute("aws-bucket-name");
                Main.awsCompress = Boolean.parseBoolean(aws.getAttribute("compress"));
            }
            int cliSz = doc.getElementsByTagName("sdfscli").getLength();
            if (cliSz > 0) {
                Element cli = (Element) doc.getElementsByTagName("sdfscli").item(0);
                Main.sdfsCliEnabled = Boolean.parseBoolean(cli.getAttribute("enable"));
                Main.sdfsCliPassword = cli.getAttribute("password");
                Main.sdfsCliSalt = cli.getAttribute("salt");
                Main.sdfsCliPort = Integer.parseInt(cli.getAttribute("port"));
                Main.sdfsCliRequireAuth = Boolean.parseBoolean(cli.getAttribute("enable-auth"));
                Main.sdfsCliListenAddr = cli.getAttribute("listen-address");
            }
        }
    }
