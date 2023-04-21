    @Override
    public List<File> write(IExtractorInputReader reader, File file) {
        List<File> files = new ArrayList<File>(reader.getExtractorRegister3D().size());
        try {
            for (IExtractorVector extractor : reader.getExtractorRegister3D()) {
                String classFileName = FileUtils.stripExtention(file);
                classFileName = classFileName.replaceAll("-" + extractor.getName(), "");
                String className = constructClassName(classFileName);
                File aFile = newFile(file.getParentFile(), className, extractor.getName());
                FileOutputStream fileOut = new FileOutputStream(aFile);
                write(className, extractor, fileOut);
                files.add(aFile);
            }
        } catch (IOException e) {
            LOG.error("Cannot write", e);
            throw new ProcessingException(e);
        }
        return files;
    }
