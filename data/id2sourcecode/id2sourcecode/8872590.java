    public final void writeSettingsObject(CrawlerSettings settings, File filename) {
        logger.fine("Writing " + filename.getAbsolutePath());
        filename.getParentFile().mkdirs();
        try {
            long lastSaved = 0L;
            File backup = null;
            if (getOrder().getController() != null && filename.exists()) {
                String name = filename.getName();
                lastSaved = settings.getLastSavedTime().getTime();
                name = name.substring(0, name.lastIndexOf('.')) + '_' + ArchiveUtils.get14DigitDate(lastSaved) + "." + settingsFilenameSuffix;
                backup = new File(filename.getParentFile(), name);
                FileUtils.copyFiles(filename, backup);
            }
            StreamResult result = new StreamResult(new BufferedOutputStream(new FileOutputStream(filename)));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source source = new CrawlSettingsSAXSource(settings);
            transformer.transform(source, result);
            if (lastSaved > (System.currentTimeMillis() - 2 * 60 * 1000)) {
                backup.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
