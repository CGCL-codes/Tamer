    @Override
    public void widgetSelected(SelectionEvent e) {
        logger.debug("SEL add site");
        Site site = new Site();
        if (DialogManager.startDialogPersitentPojo(e.display.getActiveShell(), site)) {
            File defaultResourceDir = new File(InitializationManager.getProperty("poormans.dir.defaultresources"));
            File srcDir = new File(defaultResourceDir.getAbsoluteFile(), "sites");
            File srcConfigDir = new File(srcDir, "configuration");
            File destDir = PoPathInfo.getSiteDirectory(site);
            File destConfigDir = new File(destDir, "configuration");
            destConfigDir.mkdirs();
            try {
                FileUtils.copyFileToDirectory(new File(srcDir, "format.css"), destDir);
                FileUtils.copyFileToDirectory(new File(srcConfigDir, "fckconfig.js"), destConfigDir);
                FileUtils.copyFileToDirectory(new File(srcConfigDir, "fckstyles.xml"), destConfigDir);
                site.add(buildTemplate(srcDir, "layout.html", site, null));
                File srcTemplatedir = new File(srcDir, "templates");
                site.add(buildTemplate(srcTemplatedir, "gallery.html", site, TemplateType.GALLERY));
                site.add(buildTemplate(srcTemplatedir, "image.html", site, TemplateType.IMAGE));
                site.add(buildTemplate(srcTemplatedir, "page.html", site, TemplateType.PAGE));
                Macro macro = new Macro();
                macro.setParent(site);
                macro.setName("user_menu.vm");
                macro.setText(FileTool.toString(new File(srcConfigDir, "user_menu.vm")));
            } catch (IOException e1) {
                throw new FatalException("While construct the default file structure of a site: " + e1.getMessage(), e1);
            }
            SiteHolder siteHolder = InitializationManager.getBean(SiteHolder.class);
            siteHolder.setSite(site);
            TreeViewManager treeViewManager = InitializationManager.getBean(TreeViewManager.class);
            treeViewManager.fillAndExpands(site);
            BrowserManager browserManager = InitializationManager.getBean(BrowserManager.class);
            browserManager.showHelp();
            WorkspaceToolBarManager.actionAfterSiteRenamed(site);
        }
    }
