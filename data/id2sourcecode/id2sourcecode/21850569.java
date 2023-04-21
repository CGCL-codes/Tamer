    private void processTemplate(final Configuration cfg, final String name, final String value, final File explodedTemplate, final File dest) throws IOException, TemplateException {
        final File destFile = new File(dest.getAbsolutePath() + File.separator + value.replace("/", File.separator));
        if (name.endsWith(".ftl")) {
            destFile.getParentFile().mkdirs();
            final Template template = cfg.getTemplate(name);
            final FileWriter writer = new FileWriter(destFile);
            template.process(null, writer);
            writer.close();
        } else {
            final File templateFile = new File(explodedTemplate.getAbsolutePath() + File.separator + name.replace("/", File.separator));
            if (templateFile.exists()) {
                destFile.getParentFile().mkdirs();
                FileUtils.copyFile(templateFile, destFile);
            } else if (name.equals(value)) {
                destFile.mkdirs();
            } else {
                throw new IOException("Error in template '" + name + "' doesn't exist in the template, and the destination '" + name + "' isn't the same as the name, which would have indicated it was a directory");
            }
        }
    }
