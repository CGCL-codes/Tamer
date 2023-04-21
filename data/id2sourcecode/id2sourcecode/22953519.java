    private static void createFromTemplate(IContainer destinationFolder, String templateDir, String path) throws CoreException, IOException {
        Enumeration<String> enumeration = BUNDLE.getEntryPaths(path);
        if (enumeration == null) {
            return;
        }
        for (; enumeration.hasMoreElements(); ) {
            String entryPath = enumeration.nextElement();
            if (entryPath.endsWith("/")) {
                String folderPath = entryPath.substring(templateDir.length() + 1);
                IFolder folder = destinationFolder.getFolder(new Path(folderPath));
                if (!folder.exists()) {
                    folder.create(true, true, null);
                }
                createFromTemplate(destinationFolder, templateDir, entryPath);
            } else {
                URL url = BUNDLE.getEntry(entryPath);
                String filePath = entryPath.substring(templateDir.length() + 1);
                IFile file = destinationFolder.getFile(new Path(filePath));
                if (!file.exists()) {
                    file.create(url.openStream(), IResource.FORCE, null);
                } else {
                    file.setContents(url.openStream(), true, true, null);
                }
            }
        }
    }
