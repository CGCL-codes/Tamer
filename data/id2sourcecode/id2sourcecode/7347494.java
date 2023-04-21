    public void updateFileList(String DIR) {
        debug.print("DIR=" + DIR);
        if (DIR == null) {
            return;
        }
        if (DIR.length() == 0) {
            return;
        }
        dirText.setValue("DIR: " + String.copyValueOf(DIR.toCharArray()));
        if (list != null) {
            list.clear();
        }
        File newDir = new File(DIR);
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                for (int i = 0; i < GLOBAL.extList.length; i++) {
                    if (name.toLowerCase().endsWith(GLOBAL.extList[i].toLowerCase())) {
                        return true;
                    }
                }
                File d = new File(dir.getPath() + File.separator + name);
                if (d.isDirectory()) {
                    return true;
                }
                return false;
            }
        };
        String[] files = newDir.list(filter);
        String[] sortedFiles;
        if (GLOBAL.sortOrder.equals("alphanumeric")) {
            sortedFiles = getSortedByName(DIR, files);
        } else {
            sortedFiles = getSortedByDate(DIR, files);
        }
        ViewScreen[] entries = new ViewScreen[sortedFiles.length];
        for (int i = 0; i < sortedFiles.length; i++) {
            entries[i] = new ViewScreen(getBApp(), new GLOBAL().makeEntryName(DIR, sortedFiles[i]));
        }
        if (entries.length > 0) {
            list.add(entries);
            list.setFocus(0, true);
        }
        if (files.length == 0) {
            message.setVisible(true);
        } else {
            message.setVisible(false);
        }
    }
