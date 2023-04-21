    public Mp3Tag() {
        logger = new Logger("Logger", this);
        initialize();
        ticker = new Ticker("Select Browse to load a MP3 file");
        tagEditor.setTicker(ticker);
        if (progressForm == null) {
            progressForm = new Form("Saving...");
        }
        if (gauge == null) {
            gauge = new Gauge("Progress", false, 100, 0);
            gauge.setLabel("Progress");
        }
        progressForm.append(gauge);
        showLogCommand = new Command("View Log", Command.SCREEN, 10);
        if (fileBrowser == null) fileBrowser = new FileBrowser("MP3 browser", this, "/", new String[] { "mp3" }, false, this);
        if (imageBrowser == null) imageBrowser = new FileBrowser("Image browser", this, "/", new String[] { "jpg", "png", "bmp", "gif" }, false, this);
        if (imageForm == null) imageForm = new Form("Album Art");
        replaceArtCommand = new Command("Replace", Command.SCREEN, 0);
        imageForm.addCommand(replaceArtCommand);
        showImageCmd = new Command("Album Art...", "Album Art...", Command.SCREEN, 100);
        backFromImageFormCmd = new Command("Back", "Back", Command.SCREEN, 0);
        deleteArtCmd = new Command("Delete", "Delete Image", Command.SCREEN, 1);
        imageForm.addCommand(backFromImageFormCmd);
        imageForm.setCommandListener(this);
        tagEditor.addCommand(showImageCmd);
        tagEditor.addCommand(showLogCommand);
        settingsForm = new SettingsForm(this);
        settingsForm.setListener(this);
        printf("Preferences loading");
        try {
            pref = new Preferences("settings");
            String overwrite = pref.get("overwrite");
            printf("Overwrite mode read: " + overwrite);
            if (overwrite != null) {
                settingsForm.setOvewrite(overwrite);
            }
            String sort = pref.get("sort");
            if (sort != null) {
                printf("Sort mode read: " + sort);
                settingsForm.setSort(sort);
            }
            String showTicker = pref.get("showticker");
            if (showTicker != null) {
                printf("Show ticker read: " + showTicker);
                settingsForm.setShowTicker(showTicker);
                if (showTicker.equalsIgnoreCase("No")) tagEditor.setTicker(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        configureSettingsCmd = new Command("Settings...", "Settings...", Command.SCREEN, 10);
        tagEditor.addCommand(configureSettingsCmd);
        setupImages();
    }
