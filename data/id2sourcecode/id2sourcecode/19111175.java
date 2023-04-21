    public void addHonmod(File honmod, boolean copy) throws ModNotFoundException, ModStreamException, IOException, ModZipException, ModDuplicateException {
        ArrayList<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        if (!honmod.exists()) {
            list.add(Tuple.from(honmod.getName(), "notfound"));
            throw new ModNotFoundException(list);
        }
        String xml = null;
        try {
            if (honmod.isFile()) {
                xml = new String(ZIP.getFile(honmod, Mod.MOD_FILENAME), "UTF-8");
            } else {
                xml = FileUtils.loadFile(new File(honmod, Mod.MOD_FILENAME), "UTF-8");
            }
        } catch (ZipException ex) {
            list.add(Tuple.from(honmod.getName(), "zip"));
            logger.error(ex);
            throw new ModZipException(list);
        } catch (FileNotFoundException ex) {
            list.add(Tuple.from(honmod.getName(), "zip"));
            logger.error(ex);
            throw new ModZipException(list);
        }
        Mod m = null;
        try {
            m = XML.xmlToMod(xml);
        } catch (StreamException ex) {
            list.add(Tuple.from(honmod.getName(), "stream"));
            throw new ModStreamException(list);
        }
        if (honmod.getName().endsWith(".zip")) {
            honmod.setWritable(true);
            honmod.renameTo(new File(honmod.getParentFile(), honmod.getName().replace(".zip", ".honmod")));
        }
        m.setPath(honmod.getAbsolutePath());
        if (getMod(m.getName(), m.getVersion()) != null) {
            list.add(Tuple.from(new File(getMod(m.getName(), m.getVersion()).getPath()).getName(), "duplicate"));
            list.add(Tuple.from(honmod.getName(), "duplicate"));
            throw new ModDuplicateException(list);
        }
        Icon icon;
        try {
            if (honmod.isFile()) {
                icon = new ImageIcon(ZIP.getFile(honmod, Mod.ICON_FILENAME));
            } else {
                icon = new ImageIcon(honmod.getAbsolutePath() + File.separator + Mod.ICON_FILENAME);
            }
        } catch (FileNotFoundException e) {
            icon = new javax.swing.ImageIcon(getClass().getResource("/modmanager/gui/resources/icon.png"));
        }
        String changelog = null;
        try {
            if (honmod.isFile()) {
                changelog = new String(ZIP.getFile(honmod, Mod.CHANGELOG_FILENAME));
            } else {
                changelog = FileUtils.loadFile(new File(honmod, Mod.CHANGELOG_FILENAME), null);
            }
        } catch (IOException e) {
            changelog = null;
        }
        m.setChangelog(changelog);
        m.setIcon(icon);
        logger.info("Mod file opened. Mod name: " + m.getName());
        if (copy && !(new File(ManagerOptions.getInstance().getModPath() + File.separator + honmod.getName()).exists())) {
            logger.info("Mod file copied to mods folder");
            File f = new File(ManagerOptions.getInstance().getModPath());
            f.mkdirs();
            FileUtils.copyFile(honmod, new File(f, honmod.getName()));
            logger.info("Mod file copied to mods older");
            m.setPath(f.getAbsolutePath() + File.separator + honmod.getName());
        }
        addMod(m);
    }
