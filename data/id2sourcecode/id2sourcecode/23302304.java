    protected JMenuBar createMenuBar() {
        URL url = getClass().getResource("res/Euclide.menu");
        if (url == null) {
            logger.error("Couldn't find the tools resource file: <Euclide.menu>");
            System.exit(0);
        }
        try {
            menuProps.load(url.openStream());
        } catch (FileNotFoundException ex) {
            logger.error("Couldn't find the tools resource file: <Euclide.menu>");
            System.exit(0);
        } catch (IOException ex) {
            logger.error("Error while reading file: <Euclide.menu>");
            System.exit(0);
        }
        logger.info("Menu resource file opened");
        JMenuBar mb = new JMenuBar();
        String menubarString = menuProps.getProperty("menubar", null);
        if (menubarString == null) {
            logger.warn("Warning: no menu definition in resource file");
            return mb;
        }
        for (String token : getTokens(menubarString)) {
            JMenu m = createMenu(token);
            if (m != null) mb.add(m);
        }
        return mb;
    }
