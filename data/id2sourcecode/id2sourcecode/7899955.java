    @Override
    public void dowloadProjectFile(final Project project, final OutputStream outputStream) throws IOException {
        final Long projectId = project.getId();
        final List<FilePersistence> list = projectDao.getFilesByProject(projectId);
        final ZipOutputStream zos = new ZipOutputStream(outputStream);
        try {
            final List<Language> languages = translationToolService.getLanguagesByProject(project);
            for (final Language language : languages) {
                for (final FilePersistence pFile : list) {
                    final String[] s = pFile.getFilename().split("[.]");
                    final StringBuilder filename = new StringBuilder();
                    for (int i = 0; i < s.length; i++) {
                        if (i == 0) {
                            filename.append(s[i]);
                        } else if (i == s.length - 1) {
                            filename.append("_").append(language.getLocale()).append(".").append(s[i]);
                        } else {
                            filename.append("/").append(s[i]);
                        }
                    }
                    final ZipEntry ze = new ZipEntry(filename.toString());
                    zos.putNextEntry(ze);
                    dowloadFile(pFile.toFile(), language.getLocale(), zos);
                    zos.closeEntry();
                }
            }
        } finally {
            zos.close();
        }
    }
