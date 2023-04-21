    public static String acquireExternalArtifact(String fromPath, XchangeElementContainer container) {
        fromPath = FileUtils.fixPathToBackSlash(fromPath);
        if ((fromPath == null) || fromPath.equals("")) {
            IllegalArgumentException iae = new IllegalArgumentException("A valid from path is required.");
            logger.throwing(THIS_CLASS_NAME, "acquireExternalArtifact", iae);
            throw iae;
        }
        IPersistenceHelper ph = container.getPersistenceHelper();
        File fromFile = new File(fromPath);
        if (!fromFile.exists() || fromFile.isDirectory()) {
            RuntimeException re = new RuntimeException("Prototype artifact must exist and can't be a directory");
            logger.throwing(THIS_CLASS_NAME, "acquireExternalArtifact", re);
            throw re;
        }
        String artifactsDir = ph.getDataLayout().getArtifactsDir();
        if ((artifactsDir == null) || artifactsDir.equals("")) {
            RuntimeException re = new RuntimeException("Artifact dir is not defined in DataLayout.xml");
            logger.throwing(THIS_CLASS_NAME, "acquireExternalArtifact", re);
            throw re;
        }
        String relDir = File.separator + container.getId() + File.separator;
        String extension = "";
        int lastSlashPos = fromPath.lastIndexOf(File.separator);
        int dotPos = fromPath.lastIndexOf(".");
        if (lastSlashPos < dotPos) {
            extension = fromPath.substring(fromPath.lastIndexOf("."));
        }
        String uuid = UuidUtils.getUUID();
        String relativeArtifactPath = relDir + uuid + extension;
        String artifactDir = artifactsDir + relDir;
        File artifactDirFile = new File(artifactDir);
        String absoluteArtifactPath = artifactsDir + relativeArtifactPath;
        File artifactFile;
        artifactFile = new File(absoluteArtifactPath);
        VcsProvider vcs = container.getPersistenceHelper().getDataSource().getVcsProvider();
        if (vcs != null) {
            String artifactsLimboDir = ph.getDataLayout().getArtifactsLimboDir();
            artifactDirFile = new File(artifactsLimboDir + File.separator + relDir);
            if (!artifactDirFile.exists()) {
                artifactDirFile.mkdirs();
            }
            artifactFile = new File(artifactsLimboDir + File.separator + relativeArtifactPath);
        } else {
            if (!artifactDirFile.exists()) {
                artifactDirFile.mkdirs();
            }
        }
        try {
            FileUtils.copyFile(fromFile, artifactFile);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Problem copying artifact", e);
        }
        return relativeArtifactPath;
    }
