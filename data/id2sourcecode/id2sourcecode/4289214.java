    protected MenuManager createMenuManager() {
        MenuManager bar = new MenuManager("");
        PRMenuToolbar.getInstance().fileMenu = new MenuManager(Messages.getString("item.file"), "filemenu");
        MenuManager editMenu = new MenuManager(Messages.getString("item.edit"), "editmenu");
        MenuManager winMenu = new MenuManager(Messages.getString("item.window"), "winmenu");
        MenuManager helpMenu = new MenuManager(Messages.getString("item.help"), "helpmenu");
        bar.add(PRMenuToolbar.getInstance().fileMenu);
        bar.add(editMenu);
        bar.add(winMenu);
        bar.add(helpMenu);
        PRMenuToolbar.getInstance().updateRecents();
        editMenu.add((Action) actionManager.get("addElement"));
        editMenu.add((Action) actionManager.get("viewElement"));
        editMenu.add((Action) actionManager.get("editElement"));
        editMenu.add((Action) actionManager.get("removeElement"));
        editMenu.add((Action) actionManager.get("duplicate"));
        editMenu.add((Action) actionManager.get("restoreItem"));
        editMenu.add(PRMenuToolbar.getInstance().moveToGroupMenu);
        editMenu.add(PRMenuToolbar.getInstance().moveGroupToGroupMenu);
        editMenu.add(new Separator());
        editMenu.add((Action) actionManager.get("addGroup"));
        editMenu.add((Action) actionManager.get("editGroup"));
        editMenu.add((Action) actionManager.get("deleteGroup"));
        editMenu.add(new Separator());
        editMenu.add((Action) actionManager.get("search"));
        editMenu.add(new Separator());
        editMenu.add((Action) actionManager.get("smartCopy"));
        editMenu.add((Action) actionManager.get("copyPassword"));
        editMenu.add((Action) actionManager.get("copyUser"));
        editMenu.add(new Separator());
        editMenu.add((Action) actionManager.get("clearCb"));
        winMenu.add((Action) actionManager.get("passwordColumn"));
        winMenu.add((Action) actionManager.get("iconify"));
        winMenu.add(new Separator());
        MenuManager passlistSubmenu = new MenuManager(Messages.getString("item.passlist"));
        winMenu.add(passlistSubmenu);
        winMenu.add(new Separator());
        winMenu.add((Action) actionManager.get("preferences"));
        File[] fileList = new File(LanguageAction.LANGUAGE_DIRECTORY).listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].getName().startsWith("messages") && !fileList[i].isDirectory()) {
                try {
                    String[] fileItem = fileList[i].getName().split("_");
                    if (fileItem.length == 3) {
                        Locale locale = new Locale(fileItem[1], fileItem[2].replaceAll(".properties", ""));
                        Action langaction = new LanguageAction(locale.getLanguage() + "-" + locale.getCountry(), locale.getLanguage(), locale.getCountry(), actionManager);
                        langaction.setChecked(PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.LANG).equals(locale.getLanguage()) && PreferenceManager.getInstance().getPreferenceStore().getString(PreferenceConstants.COUNTRY).equals(locale.getCountry()));
                        helpMenu.add(langaction);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        helpMenu.add(new Separator());
        if (new File("help").exists()) helpMenu.add((Action) actionManager.get("help"));
        helpMenu.add((Action) actionManager.get("website"));
        helpMenu.add(new Separator());
        helpMenu.add((Action) actionManager.get("about"));
        return bar;
    }
