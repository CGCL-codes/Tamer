    private void copyResources(String pagesDir) throws DbxFormatterException {
        final String sourceFile = MessageFormat.format("{0}/{1}/{2}", TEMPLATE_BASE_DIR, htmlTemplatesDir, MAIN_CSS_FILE_NAME);
        try {
            FileUtils.copyFileToDirectory(new File(sourceFile), new File(pagesDir));
        } catch (IOException e) {
            throw new DbxFormatterException(e);
        }
    }
