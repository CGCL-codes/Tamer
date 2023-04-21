    public void writeConfigFile() throws ParserConfigurationException, IOException {
        File dir = new File(OSValidator.getConfigPath());
        if (!dir.exists()) {
            System.out.println("making" + dir.getAbsolutePath());
            dir.mkdirs();
        }
        File file = new File(OSValidator.getConfigPath() + this.volume_name.trim() + "-volume-cfg.xml");
        Document xmldoc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation impl = builder.getDOMImplementation();
        xmldoc = impl.createDocument(null, "subsystem-config", null);
        Element root = xmldoc.getDocumentElement();
        root.setAttribute("version", Main.version);
        Element locations = xmldoc.createElement("locations");
        locations.setAttribute("dedup-db-store", this.dedup_db_store);
        locations.setAttribute("io-log", this.io_log);
        root.appendChild(locations);
        Element io = xmldoc.createElement("io");
        io.setAttribute("log-level", "1");
        io.setAttribute("chunk-size", Short.toString(this.chunk_size));
        io.setAttribute("dedup-files", Boolean.toString(this.dedup_files));
        io.setAttribute("file-read-cache", Integer.toString(this.file_read_cache));
        io.setAttribute("max-file-inactive", "900");
        io.setAttribute("max-file-write-buffers", Integer.toString(this.max_file_write_buffers));
        io.setAttribute("max-open-files", Integer.toString(this.max_open_files));
        io.setAttribute("meta-file-cache", Integer.toString(this.meta_file_cache));
        io.setAttribute("multi-read-timeout", Integer.toString(this.multi_read_timeout));
        io.setAttribute("safe-close", Boolean.toString(this.safe_close));
        io.setAttribute("safe-sync", Boolean.toString(this.safe_sync));
        io.setAttribute("system-read-cache", Integer.toString(this.system_read_cache));
        io.setAttribute("write-threads", Integer.toString(this.write_threads));
        io.setAttribute("claim-hash-schedule", this.fdisk_schedule);
        io.setAttribute("hash-size", Integer.toString(this.hashSize));
        root.appendChild(io);
        Element perm = xmldoc.createElement("permissions");
        perm.setAttribute("default-file", this.filePermissions);
        perm.setAttribute("default-folder", this.dirPermissions);
        perm.setAttribute("default-group", this.group);
        perm.setAttribute("default-owner", this.owner);
        root.appendChild(perm);
        Element vol = xmldoc.createElement("volume");
        vol.setAttribute("capacity", this.volume_capacity);
        vol.setAttribute("current-size", "0");
        vol.setAttribute("path", this.base_path + File.separator + "files");
        vol.setAttribute("maximum-percentage-full", Double.toString(this.max_percent_full));
        vol.setAttribute("closed-gracefully", "true");
        root.appendChild(vol);
        Element cs = xmldoc.createElement("local-chunkstore");
        cs.setAttribute("enabled", Boolean.toString(this.chunk_store_local));
        cs.setAttribute("pre-allocate", Boolean.toString(this.chunk_store_pre_allocate));
        cs.setAttribute("allocation-size", Long.toString(this.chunk_store_allocation_size));
        cs.setAttribute("chunk-gc-schedule", this.chunk_gc_schedule);
        cs.setAttribute("eviction-age", Integer.toString(this.remove_if_older_than));
        cs.setAttribute("gc-class", this.gc_class);
        cs.setAttribute("read-ahead-pages", Short.toString(this.chunk_read_ahead_pages));
        cs.setAttribute("chunk-store", this.chunk_store_data_location);
        cs.setAttribute("encrypt", Boolean.toString(this.chunk_store_encrypt));
        cs.setAttribute("encryption-key", this.chunk_store_encryption_key);
        cs.setAttribute("chunk-store-read-cache", Integer.toString(this.chunk_store_read_cache));
        cs.setAttribute("chunk-store-dirty-timeout", Integer.toString(this.chunk_store_dirty_timeout));
        cs.setAttribute("hash-db-store", this.chunk_store_hashdb_location);
        cs.setAttribute("chunkstore-class", this.chunk_store_class);
        Element network = xmldoc.createElement("network");
        network.setAttribute("hostname", this.list_ip);
        network.setAttribute("enable", Boolean.toString(networkEnable));
        network.setAttribute("port", Integer.toString(this.network_port));
        network.setAttribute("use-udp", Boolean.toString(this.use_udp));
        network.setAttribute("upstream-enabled", Boolean.toString(this.upstreamEnabled));
        network.setAttribute("upstream-host", this.upstreamHost);
        network.setAttribute("upstream-host-port", Integer.toString(this.upstreamPort));
        network.setAttribute("upstream-password", this.upstreamPassword);
        cs.appendChild(network);
        Element launchParams = xmldoc.createElement("launch-params");
        launchParams.setAttribute("class-path", Main.classPath);
        launchParams.setAttribute("java-path", Main.javaPath);
        long mem = calcMem(this.chunk_store_allocation_size, this.chunk_size * 1024);
        long xmn = calcXmn(this.chunk_store_allocation_size, this.chunk_size * 1024);
        launchParams.setAttribute("java-options", Main.javaOptions + " -Xmx" + mem + "m -Xmn" + xmn + "m");
        root.appendChild(launchParams);
        Element sdfscli = xmldoc.createElement("sdfscli");
        sdfscli.setAttribute("enable-auth", Boolean.toString(this.sdfsCliRequireAuth));
        sdfscli.setAttribute("listen-address", this.sdfsCliListenAddr);
        try {
            sdfscli.setAttribute("password", HashFunctions.getSHAHash(this.sdfsCliPassword.getBytes(), this.sdfsCliSalt.getBytes()));
        } catch (Exception e) {
            System.out.println("unable to create password ");
            e.printStackTrace();
            throw new IOException(e);
        }
        sdfscli.setAttribute("salt", this.sdfsCliSalt);
        sdfscli.setAttribute("port", Integer.toString(this.sdfsCliPort));
        sdfscli.setAttribute("enable", Boolean.toString(this.sdfsCliEnabled));
        root.appendChild(sdfscli);
        if (this.awsEnabled) {
            Element aws = xmldoc.createElement("aws");
            aws.setAttribute("enabled", "true");
            aws.setAttribute("aws-access-key", this.awsAccessKey);
            aws.setAttribute("aws-secret-key", this.awsSecretKey);
            aws.setAttribute("aws-bucket-name", this.awsBucketName);
            aws.setAttribute("compress", Boolean.toString(this.awsCompress));
            cs.appendChild(aws);
        } else if (this.gsEnabled) {
            Element aws = xmldoc.createElement("google-store");
            aws.setAttribute("enabled", "true");
            aws.setAttribute("gs-access-key", this.gsAccessKey);
            aws.setAttribute("gs-secret-key", this.gsSecretKey);
            aws.setAttribute("gs-bucket-name", this.gsBucketName);
            aws.setAttribute("compress", Boolean.toString(this.gsCompress));
            cs.appendChild(aws);
        }
        root.appendChild(cs);
        try {
            Source source = new DOMSource(xmldoc);
            Result result = new StreamResult(file);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
