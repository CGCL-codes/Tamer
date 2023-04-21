    private void importFiles2Folder(Map<String, URL> files, IFolder destiniFolder) throws URISyntaxException, IOException {
        for (Iterator<String> iterator = files.keySet().iterator(); iterator.hasNext(); ) {
            String fileName = iterator.next();
            URL url = (URL) files.get(fileName);
            URI sourceFileURI = FileLocator.toFileURL(url).toURI();
            File sourceFile = new File(sourceFileURI);
            URI destiniFolderURI = destiniFolder.getLocationURI();
            String destiniFileName = destiniFolderURI.getPath() + "/" + fileName.substring(fileName.lastIndexOf("/"));
            File destiniFile = new File(destiniFileName);
            FileUtils.copyFile(sourceFile, destiniFile);
        }
    }
