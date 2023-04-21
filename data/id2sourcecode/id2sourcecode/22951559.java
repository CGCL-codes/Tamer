    public static Set<String> pack(final PluginRegistry registry, final PathResolver pathResolver, final File destFile, final Filter filter) throws IOException {
        Set<String> result;
        ZipOutputStream zipStrm = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destFile, false)));
        try {
            zipStrm.setComment("JPF plug-ins archive");
            ZipEntry entry = new ZipEntry(DESCRIPTOR_ENTRY_NAME);
            entry.setComment("JPF plug-ins archive descriptor");
            zipStrm.putNextEntry(entry);
            result = writeDescripor(registry, filter, new ObjectOutputStream(zipStrm));
            zipStrm.closeEntry();
            for (PluginDescriptor descr : registry.getPluginDescriptors()) {
                if (!result.contains(descr.getUniqueId())) {
                    continue;
                }
                URL url = pathResolver.resolvePath(descr, "/");
                File file = IoUtil.url2file(url);
                if (file == null) {
                    throw new IOException("resolved URL " + url + " is not local file system location pointer");
                }
                entry = new ZipEntry(descr.getUniqueId() + "/");
                entry.setComment("Content for JPF plug-in " + descr.getId() + " version " + descr.getVersion());
                entry.setTime(file.lastModified());
                zipStrm.putNextEntry(entry);
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    packEntry(zipStrm, entry, files[i]);
                }
            }
            for (PluginFragment fragment : registry.getPluginFragments()) {
                if (!result.contains(fragment.getUniqueId())) {
                    continue;
                }
                URL url = pathResolver.resolvePath(fragment, "/");
                File file = IoUtil.url2file(url);
                if (file == null) {
                    throw new IOException("resolved URL " + url + " is not local file system location pointer");
                }
                entry = new ZipEntry(fragment.getUniqueId() + "/");
                entry.setComment("Content for JPF plug-in fragment " + fragment.getId() + " version " + fragment.getVersion());
                entry.setTime(file.lastModified());
                zipStrm.putNextEntry(entry);
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    packEntry(zipStrm, entry, files[i]);
                }
            }
        } finally {
            zipStrm.close();
        }
        return result;
    }
