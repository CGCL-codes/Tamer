    protected void setUp() throws Exception {
        super.setUp();
        journalDirectory = createTempDirectory("fedoraTestingJournalFiles");
        archiveDirectory = createTempDirectory("fedoraTestingArchiveFiles");
        lockRequestFile = new File(journalDirectory.getPath() + File.separator + "lockRequested");
        lockRequestFile.delete();
        lockAcceptedFile = new File(journalDirectory.getPath() + File.separator + "lockAccepted");
        lockAcceptedFile.delete();
        server = new MockServerForJournalTesting(DUMMY_HASH_VALUE);
        parameters = new HashMap();
        parameters.put(PARAMETER_JOURNAL_RECOVERY_LOG_CLASSNAME, "fedora.server.journal.readerwriter.multifile." + "MockJournalRecoveryLogForJournalTesting");
        parameters.put(PARAMETER_JOURNAL_READER_CLASSNAME, "fedora.server.journal.readerwriter.multifile." + "LockingFollowingJournalReader");
        parameters.put(PARAMETER_JOURNAL_DIRECTORY, journalDirectory.getPath());
        parameters.put(PARAMETER_ARCHIVE_DIRECTORY, archiveDirectory.getPath());
        parameters.put(PARAMETER_FOLLOW_POLLING_INTERVAL, "1");
        parameters.put(PARAMETER_JOURNAL_FILENAME_PREFIX, JOURNAL_FILENAME_PREFIX);
        parameters.put(PARAMETER_LOCK_REQUESTED_FILENAME, lockRequestFile.getPath());
        parameters.put(PARAMETER_LOCK_ACCEPTED_FILENAME, lockAcceptedFile.getPath());
        delegate = new MockManagementDelegateForJournalTesting();
        initialNumberOfThreads = getNumberOfCurrentThreads();
    }
