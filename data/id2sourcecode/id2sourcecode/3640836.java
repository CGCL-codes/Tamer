    private void createTabTopActions() {
        refreshAction = new Action("Refresh Channels") {

            public void run() {
                TreeItem[] roots = treeViewer.getTree().getItems();
                ArrayList list = new ArrayList();
                for (int i = 0; i < roots.length; i++) {
                    list = getAllEndNodesArbitraryType(roots[i], list, CONSTANTS.TYPE_FEED_URL);
                }
                for (int i = 0; i < roots.length; i++) {
                    list = getAllEndNodesArbitraryType(roots[i], list, CONSTANTS.TYPE_PODCAST_URL);
                }
                for (int i = 0; i < roots.length; i++) {
                    list = getAllEndNodesArbitraryType(roots[i], list, CONSTANTS.TYPE_VIDEO_URL);
                }
                final ArrayList newlist = list;
                final int[] nextId = new int[1];
                Runnable longJob = new Runnable() {

                    boolean done = false;

                    public void run() {
                        Thread thread = new Thread(new Runnable() {

                            public void run() {
                                nextId[0]++;
                                AutoDetectView.parent.getDisplay().syncExec(new Runnable() {

                                    public void run() {
                                        for (Iterator iter = newlist.iterator(); iter.hasNext(); ) {
                                            TreeItem element = (TreeItem) iter.next();
                                            TreeItem[] siblings = element.getItems();
                                            for (int i = 0; i < siblings.length; i++) {
                                                siblings[i].dispose();
                                            }
                                            NewCanalNode elementNCN = (NewCanalNode) element.getData();
                                            ArrayList kids = new ArrayList();
                                            kids = ccp.getAllChildren(elementNCN, kids);
                                            for (Iterator iterator = kids.iterator(); iterator.hasNext(); ) {
                                                NewCanalNode kid = (NewCanalNode) iterator.next();
                                                ccp.removeChild(kid);
                                            }
                                            TreeExpandFeedParser.getFeeds(element.getText(), parent, false);
                                            String[] urlList = TreeExpandFeedParser.getUrls();
                                            String[] titleList = TreeExpandFeedParser.getTitles();
                                            if (titleList == null) {
                                                return;
                                            }
                                            if (urlList == null) {
                                                return;
                                            }
                                            for (int i = 0; i < titleList.length; i++) {
                                                TreeItem ti = new TreeItem(element, SWT.NONE);
                                                if (Library.haveIReadThisArticle(urlList[i])) {
                                                    NewCanalNode cn = new NewCanalNode(titleList[i], CONSTANTS.TYPE_FEED_READ_ARTICLE);
                                                    cn.setData(urlList[i]);
                                                    ti.setImage(CONSTANTS.readArticle);
                                                    ti.setData(cn);
                                                    ti.setText(titleList[i]);
                                                    ccp.addChild((NewCanalNode) element.getData(), cn);
                                                } else {
                                                    NewCanalNode cn = new NewCanalNode(titleList[i], CONSTANTS.TYPE_FEED_UNREAD_ARTICLE);
                                                    cn.setData(urlList[i]);
                                                    ti.setImage(CONSTANTS.unReadArticle);
                                                    ti.setData(cn);
                                                    ti.setText(titleList[i]);
                                                    ccp.addChild((NewCanalNode) element.getData(), cn);
                                                }
                                            }
                                            treeViewer.expandToLevel(element.getData(), 1);
                                        }
                                    }
                                });
                                done = true;
                                AutoDetectView.parent.getDisplay().wake();
                            }
                        });
                        thread.start();
                        while (!done && !AutoDetectView.parent.getShell().isDisposed()) {
                            if (!AutoDetectView.parent.getDisplay().readAndDispatch()) AutoDetectView.parent.getDisplay().sleep();
                        }
                    }
                };
                BusyIndicator.showWhile(AutoDetectView.parent.getDisplay(), longJob);
                treeViewer.refresh();
            }
        };
        refreshAction.setEnabled(true);
        refreshAction.setToolTipText("Refresh Channels");
        refreshAction.setImageDescriptor(CONSTANTS.refeshDesc);
        followHitsAction = new Action("Follow Hits") {

            public void run() {
                followHits();
            }
        };
        followHitsAction.setEnabled(true);
        followHitsAction.setToolTipText("Follow Hits");
        followHitsAction.setImageDescriptor(CONSTANTS.followHitsImageDescriptor);
        openAction = new OpenFileAction(treeViewer, parent, ccp, CONSTANTS.AUTODETECTVIEW, this);
        openAction.setToolTipText("Open File");
        openAction.setImageDescriptor(CONSTANTS.openImageDescriptor);
        openAction = new OpenFileAction(treeViewer, parent, ccp, CONSTANTS.AUTODETECTVIEW, this);
        openAction.setToolTipText("Open File");
        openAction.setImageDescriptor(CONSTANTS.openImageDescriptor);
        emailThis = new EmailAction(treeViewer, parent, ccp, false, false);
        emailThis.setToolTipText("Email This");
        emailThis.setImageDescriptor(CONSTANTS.emailDesc);
        saveAction = new Action("Save") {

            public void run() {
                final int[] nextId = new int[1];
                Runnable longJob = new Runnable() {

                    boolean done = false;

                    public void run() {
                        Thread thread = new Thread(new Runnable() {

                            public void run() {
                                nextId[0]++;
                                AutoDetectView.parent.getDisplay().syncExec(new Runnable() {

                                    public void run() {
                                        boolean result = Writer.writeTree(ccp.getCanalGraph(), CanalContentProvider.getDefaultAutoDetectFile());
                                        if (result) {
                                            MessageDialog.openInformation(parent.getShell(), "Save", "Searches Saved");
                                            setDirty(false);
                                        } else {
                                            MessageDialog.openInformation(parent.getShell(), "Problem With Saving", "Changes Not Saved - Please try again");
                                            return;
                                        }
                                    }
                                });
                                done = true;
                                AutoDetectView.parent.getDisplay().wake();
                            }
                        });
                        thread.start();
                        while (!done && !AutoDetectView.parent.getShell().isDisposed()) {
                            if (!AutoDetectView.parent.getDisplay().readAndDispatch()) AutoDetectView.parent.getDisplay().sleep();
                        }
                    }
                };
                BusyIndicator.showWhile(AutoDetectView.parent.getDisplay(), longJob);
            }
        };
        saveAction.setToolTipText("Save Changes");
        saveAction.setImageDescriptor(BrowserApp.getImageDescriptor("save.gif"));
        saveAsAction = new Action("Save As") {

            public void run() {
                final int[] nextId = new int[1];
                Runnable longJob = new Runnable() {

                    boolean done = false;

                    public void run() {
                        Thread thread = new Thread(new Runnable() {

                            public void run() {
                                nextId[0]++;
                                AutoDetectView.parent.getDisplay().syncExec(new Runnable() {

                                    public void run() {
                                        SafeSaveDialog ssd = new SafeSaveDialog(parent.getShell(), CONSTANTS.AUTODETECTVIEW);
                                        String outFile = ssd.open();
                                        if (outFile == null) {
                                            return;
                                        }
                                        boolean test = Writer.writeTreeSaveAs(ccp.getCanalGraph(), outFile);
                                        if (test) {
                                            MessageDialog.openInformation(parent.getShell(), "Save", "All Changes Saved To Searches");
                                            setDirty(false, outFile);
                                            CanalContentProvider.setDefaultAutoDetectFile(outFile);
                                        } else {
                                            MessageDialog.openInformation(parent.getShell(), "Problem With Saving", "Changes Not Saved");
                                            return;
                                        }
                                    }
                                });
                                done = true;
                                AutoDetectView.parent.getDisplay().wake();
                            }
                        });
                        thread.start();
                        while (!done && !AutoDetectView.parent.getShell().isDisposed()) {
                            if (!AutoDetectView.parent.getDisplay().readAndDispatch()) AutoDetectView.parent.getDisplay().sleep();
                        }
                    }
                };
                BusyIndicator.showWhile(AutoDetectView.parent.getDisplay(), longJob);
            }
        };
        saveAsAction.setToolTipText("Save As");
        saveAsAction.setImageDescriptor(BrowserApp.getImageDescriptor("saveas.gif"));
        detectAction = new Action("Detect New Channels") {

            public void run() {
                final String currentURL = BrowserView.getURL();
                Job customJob = new Job("Auto Detect New Channels") {

                    protected IStatus run(IProgressMonitor monitor) {
                        monitor.beginTask("Scanning - One Moment Please", IProgressMonitor.UNKNOWN);
                        Set feedURLS = null;
                        try {
                            feedURLS = RSSAutoDetector.autoDetect(currentURL, false, null);
                        } catch (IllegalStateException ex) {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    MessageDialog.openInformation(parent.getShell(), "Auto Detect", "No Channels found on this page : Unsupported File Type");
                                }
                            });
                        }
                        if (feedURLS.size() == 0) {
                            Display.getDefault().asyncExec(new Runnable() {

                                public void run() {
                                    MessageDialog.openInformation(parent.getShell(), "Auto Detect", "No Channels found on this page");
                                }
                            });
                        } else {
                            for (Iterator iter = feedURLS.iterator(); iter.hasNext(); ) {
                                String url = ((NewCanalNode) iter.next()).getKey();
                                addFeedForAutoDetect(url, null, null, false, monitor, -1);
                            }
                        }
                        monitor.done();
                        return new Status(IStatus.OK, "framework", IStatus.OK, "Job Completed Fine", null);
                    }
                };
                JobHandler.scheduleJob(customJob, AutoDetectView.this, true, 0);
            }
        };
        detectAction.setToolTipText("Detect Channels");
        detectAction.setImageDescriptor(BrowserApp.getImageDescriptor("radar.png"));
        removeAllHits = new Action("Remove All Hits") {

            public void run() {
                removeAllHits();
            }
        };
        removeAllHits.setToolTipText("Remove All Hits");
        removeAllHits.setImageDescriptor(BrowserApp.getImageDescriptor("removeAll2.gif"));
        addBookmark = new AddBookmarkAction(parent, treeViewer);
        addBookmark.setToolTipText("Add Bookmark");
        addBookmark.setImageDescriptor(CONSTANTS.addbookmarkDesc);
        removeAHit = new Action("Remove Selected Hit") {

            public void run() {
                removeSelected();
            }
        };
        removeAHit.setToolTipText("Remove Selected Hit");
        removeAHit.setImageDescriptor(CONSTANTS.remove);
        reagg = new Action("Reaggregate Selected Hits") {

            public void run() {
                Object[] items = treeViewer.getCheckedElements();
                ArrayList validNodes = new ArrayList();
                for (int i = 0; i < items.length; i++) {
                    NewCanalNode node = (NewCanalNode) items[i];
                    if (node.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE || node.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                        validNodes.add(node);
                    }
                }
                NewCanalNode[] newnodes = new NewCanalNode[validNodes.size()];
                validNodes.toArray(newnodes);
                SafeSaveDialog ssd = new SafeSaveDialog(parent.getShell(), CONSTANTS.XMLFILEVIEW);
                String outFile = ssd.open();
                boolean result = FeedWriter.writeFeedWithDataKey(newnodes, SearchCatcher.getUrlToArticleVector(), outFile, PrefPageOne.getIntValue(CONSTANTS.PREF_SORT_ORDER));
                if (result) {
                    MessageDialog.openInformation(parent.getShell(), "Save", outFile + " Saved ");
                    BrowserView.setUrl(outFile, null);
                } else {
                    MessageDialog.openInformation(parent.getShell(), "Problem With Saving", "Changes Not Saved - Please try again");
                }
            }
        };
        reagg.setToolTipText("Reaggregate Selected Hits");
        reagg.setImageDescriptor(CONSTANTS.reagg);
        reagg.setEnabled(false);
        pdfcreate = new Action("Generate PDF From Selected Hits") {

            public void run() {
                Object[] items = treeViewer.getCheckedElements();
                ArrayList validNodes = new ArrayList();
                boolean withfeeddesc = false;
                for (int i = 0; i < items.length; i++) {
                    NewCanalNode node = (NewCanalNode) items[i];
                    if (node.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE || node.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                        validNodes.add(node);
                    } else if (node.getType() == CONSTANTS.TYPE_FEED_DESCRIPTION) {
                        withfeeddesc = true;
                    }
                }
                if (items.length == 0) {
                    MessageDialog.openInformation(parent.getShell(), "Generate PDF From Selected Hits", "Please check at least one article to create a pdf.");
                    return;
                }
                NewCanalNode[] newnodes = new NewCanalNode[validNodes.size()];
                validNodes.toArray(newnodes);
                SafeSaveDialog ssd = new SafeSaveDialog(parent.getShell(), CONSTANTS.PDFFILEVIEW);
                String outFile = ssd.open();
                ArrayList list = FeedWriter.writePDFFeed(newnodes, SearchCatcher.getUrlToArticleVector(), outFile, PrefPageOne.getIntValue(CONSTANTS.PREF_SORT_ORDER));
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(outFile);
                    PDFDocumentFactory dg = new PDFDocumentFactory(fos, true, withfeeddesc);
                    dg.createPDF(list);
                    BrowserView.setUrlInNewTab(outFile.toString(), null, true);
                } catch (FileNotFoundException e) {
                    MessageDialog.openInformation(parent.getShell(), "Generate PDF From Selected Hits", e.getMessage());
                    return;
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        };
        pdfcreate.setToolTipText("Generate PDF From Selected Hits");
        pdfcreate.setImageDescriptor(CONSTANTS.pdfDescriptor);
        pdfcreate.setEnabled(false);
        pdfdesc = new Action("Article Description") {

            public void run() {
                IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
                if (selection.isEmpty()) {
                    MessageDialog.openInformation(parent.getShell(), "Article Description", "Unable to preview, please select a search result");
                    return;
                }
                ArrayList validNodes = new ArrayList();
                for (Iterator iterator = selection.iterator(); iterator.hasNext(); ) {
                    NewCanalNode domain = (NewCanalNode) iterator.next();
                    if (domain.getType() == CONSTANTS.TYPE_FEED_READ_ARTICLE || domain.getType() == CONSTANTS.TYPE_FEED_UNREAD_ARTICLE) {
                        validNodes.add(domain);
                    }
                }
                NewCanalNode[] newnodes = new NewCanalNode[validNodes.size()];
                validNodes.toArray(newnodes);
                String defaultpdf = null;
                String temp = System.getProperty("java.io.tmpdir");
                Format formatter = new SimpleDateFormat("'Time'_HH_mm_ss_'Day'_dd_MM_yyyy");
                Date date = new Date();
                String s = formatter.format(date);
                defaultpdf = temp + s + ".pdf";
                ArrayList list = FeedWriter.writePDFFeed(newnodes, SearchCatcher.getUrlToArticleVector(), defaultpdf, PrefPageOne.getIntValue(CONSTANTS.PREF_SORT_ORDER));
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(defaultpdf);
                    PDFDocumentFactory dg = new PDFDocumentFactory(fos, false, true);
                    dg.createPDF(list);
                    fos.close();
                    BrowserView.setUrlInNewTab(defaultpdf, null, true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pdfdesc.setToolTipText("Article Description");
        pdfdesc.setImageDescriptor(CONSTANTS.pdfDescDescriptor);
        pdfdesc.setEnabled(false);
    }
