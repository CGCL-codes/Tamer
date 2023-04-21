    private void copyResource(String filterInclude, String filterExclude, IFile file, String destDir) throws CoreException, IOException {
        final IPath fullPath = file.getFullPath();
        final IPath relative = file.getParent().getProjectRelativePath();
        final String path = fullPath.toString();
        if (filterOk(path, filterInclude, filterExclude)) {
            final IPath loc = file.getLocation();
            final String dirName = destDir + File.separator;
            if (relative != null) {
                File f = new File(dirName);
                if (!f.exists()) {
                    if (!f.mkdirs()) {
                        context.getLogger().logError("Can't create directory " + dirName);
                    }
                }
                f = new File(dirName + file.getName());
                if (f.exists() && context.getConfiguration().getOptions().getResourcesCopyPolicy() != TranslatorProjectOptions.ResourcesCopyPolicy.OVERRIDE_POLICY) return;
            }
            for (final String filter : context.getConfiguration().getTranslationDescriptor().getResourcesProcessors().keySet()) {
                ResourceProcessor processor = null;
                if (file.getName().endsWith(filter)) {
                    processor = context.getConfiguration().getTranslationDescriptor().getResourcesProcessors().get(filter);
                } else {
                    processor = defaultProcessor;
                }
                context.getLogger().logInfo("Processing resource " + loc.toString());
                final Reader reader = new FileReader(loc.toString());
                final Writer writer = new PrintWriter(dirName + processor.processFilename(file.getName()));
                processor.process(reader, writer);
                reader.close();
                writer.close();
            }
        }
    }
