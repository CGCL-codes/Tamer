    private void copyReferencedFiles(File base, List referencedFiles) throws InternalErrorException {
        Iterator it = referencedFiles.iterator();
        while (it.hasNext()) {
            ResourceMapper.ResourceInfo resource = (ResourceMapper.ResourceInfo) it.next();
            if (!processedFiles.contains(resource.originalPath)) {
                File sourceFile = new File(ServicesUtil.toURI(resource.originalPath));
                if (!sourceFile.isFile()) {
                    errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("referencedFileDoesNotExist", sourceFile.getAbsolutePath(), base.getAbsolutePath()));
                } else {
                    File destFile = new File(ServicesUtil.toURI(resource.newPath));
                    try {
                        FileUtils.copyFile(sourceFile, destFile);
                    } catch (IOException e) {
                        errorReporter.warning(ErrorReporter.CSS_PROCESSOR_ORIGIN, messages.format("failedToCopyReferencedFile", new String[] { sourceFile.getAbsolutePath(), base.getAbsolutePath(), destFile.getAbsolutePath(), e.getMessage() }));
                    }
                }
                processedFiles.add(resource.originalPath);
            }
        }
    }
