    public boolean writeAboutHtmlFile(File resource, JarOutputStream out) throws Exception {
        String pluginRelativePath = eclipseInstallPlugin.getPluginLocation().getAbsolutePath().replace(eclipseArchiveLocation.getAbsolutePath(), "");
        pluginRelativePath = pluginRelativePath.substring(1);
        pluginRelativePath = pluginRelativePath.replace(File.separator, Messages.getString("Characters_entry_separator"));
        ZipEntry archiveEntry;
        if (eclipseInstallPlugin.isJar()) {
            archiveEntry = eclipseArchive.getEntry(pluginRelativePath);
            InputStream archiveInputStream = eclipseArchive.getInputStream(archiveEntry);
            JarInputStream entryInputStream = new JarInputStream(archiveInputStream);
            JarEntry jarResourceEntry = entryInputStream.getNextJarEntry();
            boolean resourceFound = false;
            while (jarResourceEntry != null && !resourceFound) {
                String resourceEntryName = jarResourceEntry.getName();
                if (resourceEntryName.equalsIgnoreCase(resource.getName())) {
                    resourceFound = true;
                } else {
                    jarResourceEntry = entryInputStream.getNextJarEntry();
                }
            }
            out.putNextEntry(new JarEntry(jarResourceEntry.getName()));
            writeToOutputStream(out, entryInputStream);
            archiveInputStream.close();
            entryInputStream.close();
        } else {
            archiveEntry = eclipseArchive.getEntry(pluginRelativePath + SLASH + resource.getName());
            out.putNextEntry(new ZipEntry(resource.getName()));
            InputStream archiveInputStream = eclipseArchive.getInputStream(archiveEntry);
            writeToOutputStream(out, archiveInputStream);
            archiveInputStream.close();
        }
        out.closeEntry();
        return true;
    }
