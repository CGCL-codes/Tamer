    protected void processTemplate(File root, String packageName, String filename, Template template, VelocityContext context, ValidationResult messages) throws IOException {
        assert (root != null) && (packageName != null) && (filename != null) && (template != null) && (context != null);
        File tempFile = File.createTempFile("pmMDA", null, null);
        tempFile.deleteOnExit();
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(tempFile)));
        writer.print(processTemplate(template, context, messages));
        writer.close();
        File file = FileOperators.createFile(root, packageName, filename);
        logTemplateProcessing(this, template, file, "{0} Catridge processing template {1} and generating file: {2}");
        String messageText = "{0} Catridge processing template {1} and generating file: {2}";
        if (!FileOperators.equals(tempFile, file)) {
            System.gc();
            file.delete();
            if (log.isInfoEnabled()) {
                log.info("writing file with new artifact content " + file.getCanonicalPath());
            }
            FileUtils.copyFile(tempFile, file);
        } else {
            messageText += " - output file has not been changed.";
        }
        Object[] arguments = { this.getPrefix(), template.getName(), file.getAbsoluteFile() };
        SimpleValidationMessage message = new SimpleValidationMessage(MessageFormat.format(messageText, arguments));
        messages.add(message);
        System.gc();
        tempFile.delete();
    }
