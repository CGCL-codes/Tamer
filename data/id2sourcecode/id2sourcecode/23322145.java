    private FileSource addOneFile(Resource resource, File file, @Nullable String sourceName) throws ImportException {
        FileSource src = new FileSource();
        if (sourceName == null) {
            sourceName = file.getName();
        }
        src.setName(sourceName);
        src.setResource(resource);
        log.debug("ADDING SOURCE " + sourceName + " FROM " + file.getAbsolutePath());
        try {
            Archive arch = ArchiveFactory.openArchive(file);
            copyArchiveFileProperties(arch.getCore(), src);
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new ImportException(e);
        } catch (UnsupportedArchiveException e) {
            log.warn(e.getMessage());
        }
        try {
            File ddFile = dataDir.sourceFile(resource, src);
            try {
                FileUtils.copyFile(file, ddFile);
            } catch (IOException e1) {
                throw new ImportException(e1);
            }
            src.setFile(ddFile);
            src.setLastModified(new Date());
            resource.addSource(src, true);
        } catch (AlreadyExistingException e) {
            throw new ImportException(e);
        }
        analyze(src);
        return src;
    }
