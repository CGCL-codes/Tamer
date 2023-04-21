    @SuppressWarnings("unchecked")
    public void testImport() throws Exception {
        File fontBaseDir = new File(System.getProperty(FONT_DIR));
        String sudoPassword = System.getProperty(PASSWORD);
        Theme theme2Import;
        Map<MenuePageType, PageSkin> skin2Import;
        File tmp;
        File targetFile;
        for (File archive : workingDir.listFiles()) {
            if (!archive.getName().toUpperCase().endsWith(".ZIP")) continue;
            System.out.println("found archive " + archive + " to extract");
            extractArchive(archive);
            File importDir = new File(workingDir, archive.getName().substring(0, archive.getName().length() - 4));
            assertTrue("import directory " + importDir.getAbsolutePath() + " does not exists", importDir.exists() && importDir.isDirectory());
            theme2Import = null;
            skin2Import = null;
            for (File archiveEntry : importDir.listFiles()) {
                if (archiveEntry.getName().toUpperCase().endsWith(".TTF")) {
                    System.err.println(archiveEntry.getName() + " should be copied to fonts directory");
                    targetFile = new File(fontBaseDir, archiveEntry.getName());
                    FileUtils.copyFile(targetFile, archiveEntry, sudoPassword);
                    continue;
                }
                if (archiveEntry.getName().endsWith(".xml")) {
                    Object any = repository.performImport(archiveEntry);
                    if (any instanceof Theme) theme2Import = (Theme) any; else if (any instanceof Map<?, ?>) skin2Import = (Map<MenuePageType, PageSkin>) any;
                }
            }
            if (theme2Import != null) {
                for (MenuePageType mpt : MenuePageType.values()) {
                    List<ThemeElement<?>> elems = theme2Import.getThemeElements(mpt);
                    for (ThemeElement<?> te : elems) {
                        if (te.getImageName() != null) {
                            targetFile = te.getImageName();
                            if (targetFile == null) continue;
                            if (targetFile.getName().length() < 2) continue;
                            tmp = new File(importDir, targetFile.getName());
                            if (tmp.exists()) {
                                System.out.println("should copy " + targetFile.getName() + " to " + targetFile);
                                FileUtils.copyFile(targetFile, tmp);
                            }
                        }
                    }
                }
            }
            if (skin2Import != null) {
                for (MenuePageType mpt : skin2Import.keySet()) {
                    PageSkin skin = skin2Import.get(mpt);
                    for (MenueElementCategory cat : skin.getElements().keySet()) {
                        ElementSkin eSkin = skin.getElementSkin(cat);
                        targetFile = eSkin.getImage();
                        if (targetFile == null) continue;
                        if (targetFile.getName().length() < 2) continue;
                        tmp = new File(importDir, targetFile.getName());
                        if (tmp.exists()) {
                            System.out.println("should copy " + targetFile.getName() + " to " + targetFile);
                            FileUtils.copyFile(targetFile, tmp);
                        }
                    }
                }
            }
        }
    }
