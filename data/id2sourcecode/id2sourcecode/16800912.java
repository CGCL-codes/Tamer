    public Theme loadTheme(String fileName) {
        Theme theme = null;
        try {
            File file = new File(fileName);
            Properties props = new Properties();
            URL url = new URL("file://" + file.getAbsolutePath());
            props.load(url.openStream());
            String themeName = props.getProperty("themeName");
            theme = new Theme(themeName);
            theme.backgroundColor = props.getProperty("backgroundColor");
            theme.textColor = props.getProperty("textColor");
            theme.titleIcon = props.getProperty("titleIcon");
            theme.shareRapIcon = props.getProperty("shareRapIcon");
            theme.informationSelectionIcon = props.getProperty("informationSelectionIcon");
            theme.developSelectionIcon = props.getProperty("developSelectionIcon");
            theme.viewSelectionIcon = props.getProperty("viewSelectionIcon");
            theme.printSelectionIcon = props.getProperty("printSelectionIcon");
            theme.helpSelectionIcon = props.getProperty("helpSelectionIcon");
            theme.newProjectIcon = props.getProperty("newProjectIcon");
            theme.saveProjectIcon = props.getProperty("saveProjectIcon");
            theme.uploadProjectIcon = props.getProperty("uploadProjectIcon");
            theme.closeProjectIcon = props.getProperty("closeProjectIcon");
            theme.deleteProjectIcon = props.getProperty("deleteProjectIcon");
        } catch (Exception e) {
            System.out.println("Unable to load theme " + fileName);
            theme = null;
        }
        return theme;
    }
