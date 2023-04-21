    protected void save(OutputStream out) throws IOException {
        logger.debug("Saving project...");
        Properties props = new Properties();
        props.put(PROPERTY_KEY_PROJECT_TYPE, this.getClass().getCanonicalName());
        props.put(PROPERTY_KEY_RESSOURCE_COUNT, "" + ressources.size());
        int i = 0;
        for (Ressource r : ressources) {
            props.put(PROPERTY_KEY_RESSOURCE_TYPE_PREFIX + (i++) + PROPERTY_KEY_RESSOURCE_TYPE_POSTFIX, r.getClass().getCanonicalName());
        }
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(out));
        ZipEntry entry = new ZipEntry("project.properties");
        zipOut.putNextEntry(entry);
        props.store(zipOut, null);
        logger.debug("Properties saved");
        logger.debug("Saving " + ressources.size() + " ressources...");
        i = 0;
        for (Ressource r : ressources) {
            entry = new ZipEntry("project.ressource" + (i++) + ".data");
            zipOut.putNextEntry(entry);
            r.save(zipOut);
        }
        logger.debug("Ressources saved");
        zipOut.close();
        logger.debug("Project saved");
    }
