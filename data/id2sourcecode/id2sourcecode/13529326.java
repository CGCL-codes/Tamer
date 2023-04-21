    public void parseCmdLine(String[] args) throws Exception {
        CommandLineParser parser = new PosixParser();
        Options options = buildOptions();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("--help")) {
            printHelp(options);
            System.exit(1);
        }
        if (!cmd.hasOption("volume-name") || !cmd.hasOption("volume-capacity")) {
            System.out.println("--volume-name and --volume-capacity are required options");
            printHelp(options);
            System.exit(-1);
        }
        volume_name = cmd.getOptionValue("volume-name");
        this.volume_capacity = cmd.getOptionValue("volume-capacity");
        base_path = OSValidator.getProgramBasePath() + "volumes" + File.separator + volume_name;
        if (cmd.hasOption("base-path")) {
            this.base_path = cmd.getOptionValue("base-path");
        }
        this.io_log = this.base_path + File.separator + "ioperf.log";
        this.dedup_db_store = this.base_path + File.separator + "ddb";
        this.chunk_store_data_location = this.base_path + File.separator + "chunkstore" + File.separator + "chunks";
        this.chunk_store_hashdb_location = this.base_path + File.separator + "chunkstore" + File.separator + "hdb";
        if (cmd.hasOption("dedup-db-store")) {
            this.dedup_db_store = cmd.getOptionValue("dedup-db-store");
        }
        if (cmd.hasOption("io-log")) {
            this.io_log = cmd.getOptionValue("io-log");
        }
        if (cmd.hasOption("io-safe-close")) {
            this.safe_close = Boolean.parseBoolean(cmd.getOptionValue("io-safe-close"));
        }
        if (cmd.hasOption("hash-size")) {
            int hs = Integer.parseInt(cmd.getOptionValue("hash-size"));
            if (hs == 16 || hs == 24) this.hashSize = hs; else throw new Exception("hash size must be 16 or 24");
        }
        if (cmd.hasOption("chunkstore-class")) {
            this.chunk_store_class = cmd.getOptionValue("chunkstore-class");
        }
        if (cmd.hasOption("io-safe-sync")) {
            this.safe_sync = Boolean.parseBoolean(cmd.getOptionValue("io-safe-sync"));
        }
        if (cmd.hasOption("io-write-threads")) {
            this.write_threads = Short.parseShort(cmd.getOptionValue("io-write-threads"));
        }
        if (cmd.hasOption("io-dedup-files")) {
            this.dedup_files = Boolean.parseBoolean(cmd.getOptionValue("io-dedup-files"));
        }
        if (cmd.hasOption("io-multi-read-timeout")) {
            this.multi_read_timeout = Integer.parseInt(cmd.getOptionValue("io-multi-read-timeout"));
        }
        if (cmd.hasOption("io-system-read-cache")) {
            this.system_read_cache = Integer.parseInt(cmd.getOptionValue("io-system-read-cache"));
        }
        if (cmd.hasOption("io-chunk-size")) {
            this.chunk_size = Short.parseShort(cmd.getOptionValue("io-chunk-size"));
        }
        if (cmd.hasOption("io-max-file-write-buffers")) {
            this.max_file_write_buffers = Integer.parseInt(cmd.getOptionValue("io-max-file-write-buffers"));
        } else {
            this.max_file_write_buffers = 1;
        }
        if (cmd.hasOption("io-max-open-files")) {
            this.max_open_files = Integer.parseInt(cmd.getOptionValue("io-max-open-files"));
        }
        if (cmd.hasOption("io-file-read-cache")) {
            this.file_read_cache = Integer.parseInt(cmd.getOptionValue("io-file-read-cache"));
        }
        if (cmd.hasOption("io-meta-file-cache")) {
            this.meta_file_cache = Integer.parseInt(cmd.getOptionValue("io-meta-file-cache"));
        }
        if (cmd.hasOption("io-claim-chunks-schedule")) {
            this.fdisk_schedule = cmd.getOptionValue("io-claim-chunks-schedule");
        }
        if (cmd.hasOption("permissions-file")) {
            this.filePermissions = cmd.getOptionValue("permissions-file");
        }
        if (cmd.hasOption("permissions-folder")) {
            this.dirPermissions = cmd.getOptionValue("permissions-folder");
        }
        if (cmd.hasOption("permissions-owner")) {
            this.owner = cmd.getOptionValue("permissions-owner");
        }
        if (cmd.hasOption("chunk-store-data-location")) {
            this.chunk_store_data_location = cmd.getOptionValue("chunk-store-data-location");
        }
        if (cmd.hasOption("chunk-store-hashdb-location")) {
            this.chunk_store_hashdb_location = cmd.getOptionValue("chunk-store-hashdb-location");
        }
        if (cmd.hasOption("chunk-store-pre-allocate")) {
            this.chunk_store_pre_allocate = Boolean.parseBoolean(cmd.getOptionValue("chunk-store-pre-allocate"));
        }
        if (cmd.hasOption("sdfscli-password")) {
            this.sdfsCliPassword = cmd.getOptionValue("sdfscli-password");
        }
        if (cmd.hasOption("sdfscli-requre-auth")) {
            this.sdfsCliRequireAuth = true;
        }
        if (cmd.hasOption("sdfscli-listen-port")) {
            this.sdfsCliPort = Integer.parseInt(cmd.getOptionValue("sdfscli-listen-port"));
        }
        if (cmd.hasOption("sdfscli-listen-addr")) this.sdfsCliListenAddr = cmd.getOptionValue("sdfscli-listen-addr");
        if (cmd.hasOption("chunk-read-ahead-pages")) {
            this.chunk_read_ahead_pages = Short.parseShort(cmd.getOptionValue("chunk-read-ahead-pages"));
        } else {
            if (this.chunk_size < 32) {
                this.chunk_read_ahead_pages = (short) (32 / this.chunk_size);
            } else {
                this.chunk_read_ahead_pages = 1;
            }
        }
        if (cmd.hasOption("chunk-store-local")) {
            this.chunk_store_local = Boolean.parseBoolean((cmd.getOptionValue("chunk-store-local")));
        }
        if (cmd.hasOption("aws-enabled")) {
            this.awsEnabled = Boolean.parseBoolean(cmd.getOptionValue("aws-enabled"));
        }
        if (cmd.hasOption("chunk-store-read-cache")) {
            this.chunk_store_read_cache = Integer.parseInt(cmd.getOptionValue("chunk-store-read-cache"));
        }
        if (cmd.hasOption("chunk-store-encrypt")) {
            this.chunk_store_encrypt = Boolean.parseBoolean(cmd.getOptionValue("chunk-store-encrypt"));
        }
        if (cmd.hasOption("chunk-store-dirty-timeout")) {
            this.chunk_store_dirty_timeout = Integer.parseInt(cmd.getOptionValue("chunk-store-dirty-timeout"));
        }
        if (cmd.hasOption("gc-class")) {
            this.gc_class = cmd.getOptionValue("gc-class");
        }
        if (this.awsEnabled) {
            if (cmd.hasOption("aws-secret-key") && cmd.hasOption("aws-access-key") && cmd.hasOption("aws-bucket-name")) {
                this.awsAccessKey = cmd.getOptionValue("aws-access-key");
                this.awsSecretKey = cmd.getOptionValue("aws-secret-key");
                this.awsBucketName = cmd.getOptionValue("aws-bucket-name");
                if (!cmd.hasOption("io-chunk-size")) this.chunk_size = 128;
                if (!S3ChunkStore.checkAuth(awsAccessKey, awsSecretKey)) {
                    System.out.println("Error : Unable to create volume");
                    System.out.println("aws-access-key or aws-secret-key is incorrect");
                    System.exit(-1);
                }
                if (!S3ChunkStore.checkBucketUnique(awsAccessKey, awsSecretKey, awsBucketName)) {
                    System.out.println("Error : Unable to create volume");
                    System.out.println("aws-bucket-name is not unique");
                    System.exit(-1);
                }
            } else {
                System.out.println("Error : Unable to create volume");
                System.out.println("aws-access-key, aws-secret-key, and aws-bucket-name are required.");
                System.exit(-1);
            }
            if (cmd.hasOption("aws-compress")) this.awsCompress = Boolean.parseBoolean(cmd.getOptionValue("aws-compress"));
        } else if (this.gsEnabled) {
            if (cmd.hasOption("gs-secret-key") && cmd.hasOption("gs-access-key") && cmd.hasOption("gs-bucket-name")) {
                this.awsAccessKey = cmd.getOptionValue("gs-access-key");
                this.awsSecretKey = cmd.getOptionValue("gs-secret-key");
                this.awsBucketName = cmd.getOptionValue("gs-bucket-name");
                if (!cmd.hasOption("io-chunk-size")) this.chunk_size = 128;
            } else {
                System.out.println("Error : Unable to create volume");
                System.out.println("gs-access-key, gs-secret-key, and gs-bucket-name are required.");
                System.exit(-1);
            }
            if (cmd.hasOption("gs-compress")) this.awsCompress = Boolean.parseBoolean(cmd.getOptionValue("aws-compress"));
        }
        if (cmd.hasOption("chunk-store-gc-schedule")) {
            this.chunk_gc_schedule = cmd.getOptionValue("chunk-store-gc-schedule");
        }
        if (cmd.hasOption("chunk-store-eviction")) {
            this.remove_if_older_than = Integer.parseInt(cmd.getOptionValue("chunk-store-eviction"));
        }
        if (cmd.hasOption("volume-maximum-full-percentage")) {
            this.max_percent_full = Double.parseDouble(cmd.getOptionValue("volume-maximum-full-percentage"));
        }
        if (cmd.hasOption("chunk-store-size")) {
            this.chunk_store_allocation_size = StringUtils.parseSize(cmd.getOptionValue("chunk-store-size"));
        } else {
            this.chunk_store_allocation_size = StringUtils.parseSize(this.volume_capacity);
        }
        if (cmd.hasOption("dse-enable-network")) {
            this.networkEnable = true;
        }
        if (cmd.hasOption("dse-enable-udp")) {
            this.use_udp = true;
        }
        if (cmd.hasOption("dse-listen-ip")) {
            this.list_ip = cmd.getOptionValue("dse-listen-ip");
            this.networkEnable = true;
        }
        if (cmd.hasOption("dse-listen-port")) {
            this.network_port = Integer.parseInt(cmd.getOptionValue("listen-port"));
        }
        if (cmd.hasOption("dse-upstream-enabled")) {
            if (!cmd.hasOption("dse-upstream-host")) {
                throw new Exception("dse-upstream-host must be specified");
            } else {
                this.upstreamHost = cmd.getOptionValue("dse-upstream-host");
                if (cmd.hasOption("dse-upstream-host-port")) this.upstreamPort = Integer.parseInt(cmd.getOptionValue("dse-upstream-host-port"));
            }
        }
        if (cmd.hasOption("dse-upstream-password")) this.upstreamPassword = cmd.getOptionValue("dse-upstream-password");
        if (cmd.hasOption("enable-replication-master")) {
            this.sdfsCliRequireAuth = true;
            this.sdfsCliListenAddr = "0.0.0.0";
            this.networkEnable = true;
        }
        if (cmd.hasOption("enable-replication-slave")) {
            if (!cmd.hasOption("replication-master")) throw new Exception("replication-master must be specified");
            this.upstreamHost = cmd.getOptionValue("replication-master");
            this.upstreamEnabled = true;
        }
        if (cmd.hasOption("replication-master-password")) this.upstreamPassword = cmd.getOptionValue("replication-master-password");
        File file = new File(OSValidator.getConfigPath() + this.volume_name.trim() + "-volume-cfg.xml");
        if (file.exists()) {
            throw new IOException("Volume [" + this.volume_name + "] already exists");
        }
    }
