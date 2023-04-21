    public static void writeArchive(LWMap map, File archive) throws java.io.IOException {
        Log.info("Writing archive package " + archive);
        final String label = archive.getName();
        final String mapName;
        if (label.endsWith(VueUtil.VueArchiveExtension)) mapName = label.substring(0, label.length() - 4); else mapName = label;
        final String dirName = mapName + ".vdr";
        final Collection<Resource> uniqueResources = map.getAllUniqueResources();
        final Collection<PropertyEntry> manifest = new ArrayList();
        final List<Item> items = new ArrayList();
        final Set<String> uniqueEntryNames = new HashSet();
        Archive.UniqueNameFailsafeCount = 1;
        for (Resource r : uniqueResources) {
            try {
                final File sourceFile = r.getActiveDataFile();
                final String description = "" + (DEBUG.Enabled ? r : r.getSpec());
                if (sourceFile == null) {
                    Log.info("skipped: " + description);
                    continue;
                } else if (!sourceFile.exists()) {
                    Log.warn("Missing local file: " + sourceFile + "; for " + r);
                    continue;
                }
                final String packageEntryName = generatePackageFileName(r, uniqueEntryNames);
                final ZipEntry entry = new ZipEntry(dirName + "/" + packageEntryName);
                Archive.setComment(entry, "\t" + SPEC_KEY + r.getSpec());
                final Item item = new Item(entry, r, sourceFile);
                items.add(item);
                manifest.add(new PropertyEntry(r.getSpec(), packageEntryName));
                if (DEBUG.Enabled) Log.info("created: " + item);
            } catch (Throwable t) {
                Log.error("writeArchive: failed to handle " + Util.tags(r), t);
            }
        }
        final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(archive)));
        final ZipEntry mapEntry = new ZipEntry(dirName + "/" + mapName + "$map.vue");
        final String comment = MAP_ARCHIVE_KEY + "; VERSION: 2;" + " Saved " + new Date() + " by " + VUE.getName() + " built " + Version.AllInfo + "; items=" + items.size() + ";" + ">";
        Archive.setComment(mapEntry, comment);
        zos.putNextEntry(mapEntry);
        final Writer mapOut = new OutputStreamWriter(zos);
        try {
            map.setArchiveManifest(manifest);
            ActionUtil.marshallMapToWriter(map, mapOut);
        } catch (Throwable t) {
            Log.error(t);
            throw new RuntimeException(t);
        } finally {
            map.setArchiveManifest(null);
        }
        for (Item item : items) {
            if (DEBUG.Enabled) Log.debug("writing: " + item); else Log.info("writing: " + item.entry);
            try {
                zos.putNextEntry(item.entry);
                copyBytesToZip(item.dataFile, zos);
            } catch (Throwable t) {
                Log.error("Failed to archive item: " + item, t);
            }
        }
        zos.closeEntry();
        zos.close();
        Log.info("Wrote " + archive);
    }
