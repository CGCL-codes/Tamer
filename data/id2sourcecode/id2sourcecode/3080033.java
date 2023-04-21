    protected Store(File rootDir, StartMode mode) throws IOException {
        validator.notNull(mode, "null mode");
        this.rootDir = rootDir;
        logName = "[" + new AbbreviatedFilepath(rootDir) + "]: ";
        logger.info(logName + "initializing segment store: " + mode);
        logger.finer(logName + "root directory: " + rootDir);
        if (!mode.mustExist()) {
            rootDir.mkdirs();
        }
        if (!rootDir.isDirectory()) {
            validator.fail(logName + "the specified root directory could not be " + (mode.mustExist() ? "created: " : "found: ") + rootDir);
        }
        this.xprocLock = new XprocLock(new File(rootDir, LOCK_FILE));
        xprocLock.lock();
        File versionFile = new File(rootDir, VERSION_FILE);
        boolean create;
        if (versionFile.exists()) {
            validator.isFalse(mode.mustNotExist(), logName + VERSION_FILE + " already exists");
            getFileVersion();
            create = false;
        } else {
            validator.isFalse(mode.mustExist(), logName + "could not find " + VERSION_FILE);
            createVersionFile(versionFile);
            create = true;
        }
        File committedDir = new File(rootDir, COMMITTED_DIR);
        this.collector = new Collector(committedDir, create);
        {
            File txnGapFile = new File(committedDir, TXN_GAP_FILE);
            FileChannel channel = new RandomAccessFile(txnGapFile, "rw").getChannel();
            this.txnGapTable = new TxnGapTable(channel, logName);
        }
        File pendingDirWork = new File(rootDir, PENDING_DIR);
        if (create) {
            pendingDirWork.mkdir();
            validator.isTrue(pendingDirWork.isDirectory(), "failed to create pending directory");
            validator.isTrue(pendingDirWork.list().length == 0, "non-empty pending directory");
        } else {
            validator.isTrue(pendingDirWork.isDirectory(), "pending directory not found");
            if (pendingDirWork.listFiles(UNIT_DIR_FILTER).length != 0) {
                logger.warning("Moving aborted transactions to " + ABORTED_DIR + " directory..");
                File abortedDir = new File(rootDir, ABORTED_DIR);
                abortedDir.mkdir();
                validator.isTrue(abortedDir.isDirectory(), "failed to create aborted directory");
                File backupDir = new File(abortedDir, abortedTxnsTimestamp());
                validator.isTrue(pendingDirWork.renameTo(backupDir), "failed to backup " + PENDING_DIR + " dir to " + new AbbreviatedFilepath(backupDir));
                pendingDirWork = new File(rootDir, PENDING_DIR);
                pendingDirWork.mkdir();
                validator.isTrue(pendingDirWork.isDirectory(), "failed to create empty " + PENDING_DIR + " dir");
            }
        }
        this.pendingDir = pendingDirWork;
    }
