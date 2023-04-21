    public void run() {
        try {
            isAlive = true;
            cancel = false;
            value = 0;
            min = 0;
            max = 90;
            name = "Building navigation tree.";
            status = "Loading contents.xml.gz";
            Thread.yield();
            URL urlContents = LOADER.getURL(contentsFile);
            System.out.println("Contents file: " + urlContents.toString());
            JarURLConnection jurlc = (JarURLConnection) urlContents.openConnection();
            InputStream is = jurlc.getInputStream();
            value = 10;
            status = "Uncompressing contents.xml.gz";
            Thread.yield();
            NavigationTreeBuilder navigationTreeBuilder = new NavigationTreeBuilder(this, is);
            navigationTreeBuilder.run();
            value = 60;
            status = "Generating tree view.";
            Thread.yield();
            ntnRoot = navigationTreeBuilder.getNavigationTreeRoot();
            navigationTreeViewSetter = new NavigationTreeViewSetter(ntnRoot, metricFrame.getNavigationPanel());
            value = 70;
            status = "Displaying tree";
            Thread.yield();
            if (navigationView.equals("edexcel")) {
                navigationTreeViewSetter.setNavigationTreeView(NavigationTreeView.EDEXCEL_SYLLABUS_ACTIVITIES_ONLY);
            } else if (navigationView.equals("topic")) {
                metricFrame.navViewPanel.jbutTopic.setSelected(true);
                metricFrame.navViewPanel.enableControls();
                navigationTreeViewSetter.setNavigationTreeView(NavigationTreeView.SUBJECT_ACTIVITIES_ONLY);
            } else {
                navigationTreeViewSetter.setNavigationTreeView(NavigationTreeView.DEFAULT);
            }
            metricFrame.setNavVsWorkingAndNotesDividerLocation();
            value = 80;
            status = "Finishing up.";
            Thread.yield();
            metricFrame.setUpSearchPanel(navigationTreeBuilder.getNameSearchHashMap(), navigationTreeBuilder.getSynopsisSearchHashMap());
        } catch (Exception e) {
            EXCEPTION_PROCESSOR.process(e);
        } finally {
            isAlive = false;
        }
    }
