    private DedupFileChannel getFileChannel(String path) throws FuseException {
        File f = this.resolvePath(path);
        try {
            MetaDataDedupFile mf = MetaFileStore.getMF(f);
            return mf.getDedupFile().getChannel();
        } catch (IOException e) {
            SDFSLogger.getLog().error("unable to open file" + path, e);
            throw new FuseException("error opening " + path).initErrno(FuseException.EACCES);
        }
    }
