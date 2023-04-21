    public static void saveSettings(String newSetting) {
        FileWriter f = null;
        try {
            String dir = Main.PROG_DIR;
            f = new FileWriter(dir + "bump3.ini");
            f.write("min_filesize=" + Main.MIN_FILESIZE + "\n");
            f.write("save_dir=" + Main.SAVE_DIR + "\n");
            f.write("autoplay=" + Main.AUTOPLAY + "\n");
            f.write("autoplay_wait=" + Main.AUTOPLAY_WAIT + "\n");
            f.write("playstring=" + Main.PLAYSTRING + "\n");
            f.write("connect_timeout=" + Main.CONNECT_TIMEOUT + "\n");
            f.write("read_timeout=" + Main.READ_TIMEOUT + "\n");
            f.write("theme=" + Main.THEME + "\n");
            f.write("agreed=" + Main.AGREED + "\n");
            f.write("nospaces=" + Main.NOSPACES + "\n");
            f.write("nodupes=" + Main.NODUPES + "\n");
            if (Main.RECENTS != null) f.write("recent_files=" + join(Main.RECENTS, ";") + "\n");
            if (Main.theGUI != null) f.write("engines=" + join(Gui.mnuEngines, ",") + "\n");
            if (Main.DOWNLOADED != null && Main.NODUPES) f.write("downloaded_urls=" + join(Main.DOWNLOADED, ";") + "\n");
            if (!newSetting.equals("")) {
                f.write(newSetting + "\n");
            }
        } catch (IOException ioe) {
            System.out.println("ERROR! " + ioe.getMessage());
        } finally {
            try {
                if (f != null) f.close();
            } catch (IOException ioe) {
            }
        }
    }
