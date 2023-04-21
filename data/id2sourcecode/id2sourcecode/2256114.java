    public void extractProjects(String archiveName) throws IOException {
        byte[] buffer = new byte[4096];
        Set newProjects = new HashSet();
        InputStream is = getClass().getResourceAsStream(archiveName);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            String entryName = entry.getName();
            String projectName = getProjectName(entryName);
            if (entry.isDirectory()) {
                if (!projectExists(projectName)) {
                    createProject(projectName);
                    newProjects.add(projectName);
                }
            } else {
                if (newProjects.contains(projectName)) {
                    String filename = getProjectFileName(entryName);
                    FileOutputStream fos = new FileOutputStream(toProjectFile(projectName, filename));
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int n;
                    while ((n = zis.read(buffer, 0, 4096)) != -1) bos.write(buffer, 0, n);
                    bos.close();
                }
            }
        }
    }
