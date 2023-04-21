    public Xml(String infId, Properties props, org.smslib.smsserver.SMSServer server, InterfaceTypes type) {
        super(infId, props, server, type);
        description = "Interface for xml input/output files";
        processedFiles = new HashMap();
        inDirectory = new File(getProperty("in") == null ? "." : getProperty("in"));
        outDirectory = new File(getProperty("out") == null ? "." : getProperty("out"));
        if (isInbound()) {
            if (!inDirectory.isDirectory() || !inDirectory.canWrite()) {
                throw new IllegalArgumentException(infId + ".in isn't a directory or isn't write-/readable!");
            }
            try {
                writeInboundDTD(inDirectory);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        if (isOutbound()) {
            if (!outDirectory.isDirectory() || !outDirectory.canRead() || !outDirectory.canWrite()) {
                throw new IllegalArgumentException(infId + ".out isn't a directory or isn't write-/readable!");
            }
            outSentDirectory = new File(outDirectory, sOutSentDirectory);
            if (!outSentDirectory.isDirectory()) {
                if (!outSentDirectory.mkdir()) {
                    throw new IllegalArgumentException("Can't create directory '" + outSentDirectory);
                }
            }
            outFailedDirectory = new File(outDirectory, sOutFailedDirectory);
            if (!outFailedDirectory.isDirectory()) {
                if (!outFailedDirectory.mkdir()) {
                    throw new IllegalArgumentException("Can't create directory '" + outFailedDirectory);
                }
            }
            outBrokenDirectory = new File(outDirectory, sOutBrokenDirectory);
            if (!outBrokenDirectory.isDirectory()) {
                if (!outBrokenDirectory.mkdir()) {
                    throw new IllegalArgumentException("Can't create directory '" + outBrokenDirectory);
                }
            }
            try {
                writeOutboundDTD(outDirectory);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
