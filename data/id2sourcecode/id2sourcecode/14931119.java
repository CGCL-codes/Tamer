    public boolean addImageToRepository(final String fullFileName, final String code, final String pathToRepository) throws IOException {
        File file = new File(fullFileName);
        String pathInRepository = pathToRepository + getImageNameStrategy(StringUtils.EMPTY).getFullFileNamePath(file.getName(), code);
        File destinationFile = new File(pathInRepository);
        FileUtils.copyFile(file, destinationFile);
        return destinationFile.exists();
    }
