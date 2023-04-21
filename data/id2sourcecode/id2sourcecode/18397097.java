    public void testExport() throws Exception {
        Theme theme2Export = menueDef.getTheme();
        Map<MenuePageType, PageSkin> skin2Export = menueDef.getSkin();
        File exportDir = new File(workingDir, theme2Export.getName());
        File themeExportFile = new File(exportDir, "sampleTheme.xml");
        File skinExportFile = new File(exportDir, "sampleSkin.xml");
        File targetFile;
        exportDir.mkdirs();
        repository.performExport(theme2Export, themeExportFile);
        repository.performExport(skin2Export, "SampleSkin", skinExportFile);
        for (MenuePageType mpt : MenuePageType.values()) {
            List<ThemeElement<?>> elems = theme2Export.getThemeElements(mpt);
            for (ThemeElement<?> te : elems) {
                if (te.getImageName() != null) {
                    System.err.println("check image [ " + te.getImageName().getAbsolutePath() + " ]");
                    targetFile = new File(exportDir, te.getImageName().getName());
                    if (!targetFile.exists()) FileUtils.copyFile(targetFile, te.getImageName());
                }
            }
        }
        File tmp;
        for (MenuePageType mpt : skin2Export.keySet()) {
            PageSkin skin = skin2Export.get(mpt);
            for (MenueElementCategory cat : skin.getElements().keySet()) {
                ElementSkin eSkin = skin.getElementSkin(cat);
                if (eSkin.getFont() != null) {
                    System.out.println("check font [" + eSkin.getFont() + "]");
                    if (fontCache.containsKey(eSkin.getFont())) {
                        for (File fontFile : fontCache.get(eSkin.getFont())) {
                            targetFile = new File(exportDir, fontFile.getName());
                            if (!targetFile.exists()) FileUtils.copyFile(targetFile, fontFile);
                        }
                    }
                }
                if (eSkin.getImage() != null) {
                    tmp = eSkin.getImage();
                    if (tmp.isFile() && tmp.canRead()) {
                        System.out.println("check image " + tmp.getAbsolutePath());
                        targetFile = new File(exportDir, tmp.getName());
                        if (tmp.isFile() && tmp.canRead() && !targetFile.exists()) {
                            FileUtils.copyFile(targetFile, tmp);
                        }
                    }
                }
            }
        }
        createArchive(exportDir);
        assertTrue("failed to create archive", new File(workingDir, exportDir.getName() + ".zip").exists());
        FileUtils.removeDirectory(exportDir);
    }
