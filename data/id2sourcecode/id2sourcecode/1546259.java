    public void process() throws Exception {
        if (!automaticUpdate) {
            System.out.println("** Automatic Updating Disabled ***");
        }
        if (support) {
            List<File> locations = new ArrayList<File>();
            for (String file : files) {
                File dir = new File(file);
                if (dir.exists() && dir.isDirectory()) {
                    locations.add(dir);
                } else {
                    System.out.println("Skipping Location: " + dir.getAbsolutePath());
                }
            }
            System.out.println("Creating Support Zip File...");
            File out = Troubleshooter.createSupportZip("No Description: BMT Commandline", true, true, true);
            System.out.println("ZipFile Created: " + out.getPath());
            return;
        }
        if (listProvders) {
            renderProviders(Phoenix.getInstance().getMetadataManager().getProviders());
            return;
        }
        IMediaFolder parentFolder = null;
        List<IMediaResource> resources = new ArrayList<IMediaResource>();
        for (String f : files) {
            File file = new File(f);
            if (!file.exists() && offline == false) {
                System.out.println("File Not Found: " + file.getAbsolutePath() + "\nConsider adding --offline=true if you want to create an offline video.");
            } else {
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        log.warn("Failed to create file: " + file);
                    }
                }
                IMediaResource r = FileResourceFactory.createResource(null, file);
                resources.add(r);
            }
        }
        if (resources.size() == 1 && (resources.get(0) instanceof IMediaFolder)) {
            parentFolder = (IMediaFolder) resources.get(0);
        } else {
            parentFolder = new VirtualMediaFolder(null, "BMT Videos");
            for (IMediaResource r : resources) {
                ((VirtualMediaFolder) parentFolder).addMediaResource(r);
            }
        }
        if (parentFolder.getChildren().size() == 0) {
            System.out.println("No Files to process.");
        } else {
            if (listMedia) {
                parentFolder.accept(new ListMovieVisitor(false), new BasicProgressMonitor(), IMediaResource.DEEP_UNLIMITED);
                return;
            }
            ProgressTracker<IMediaFile> progress = new ProgressTracker<IMediaFile>(new ConsoleProgressMonitor());
            if (automaticUpdate) {
                parentFolder.accept(new AutomaticMetadataVisitor(null), progress, IMediaResource.DEEP_UNLIMITED);
            } else {
                parentFolder.accept(new ConsoleInteractiveMetadataVisitor(displaySize, null), progress, IMediaResource.DEEP_UNLIMITED);
            }
            System.out.printf("         Updated: %s items\n", progress.getSuccessfulItems().size());
            System.out.printf("Failed to Update: %s items\n", progress.getFailedItems().size());
            if (automaticUpdate && progress.getFailedItems().size() > 0 && prompt) {
                while (!progress.isCancelled() && progress.getFailedItems().peek() != null) {
                    TrackedItem<IMediaFile> item = progress.getFailedItems().pop();
                    System.out.println("  File: " + PathUtils.getLocation(item.getItem()));
                    System.out.println("Reason: " + item.getMessage());
                    System.out.println("---------------------------------");
                    item.getItem().accept(new ConsoleInteractiveMetadataVisitor(displaySize, null), new ConsoleProgressMonitor(), IMediaResource.DEEP_UNLIMITED);
                    System.out.println("---------------------------------");
                }
            } else {
                for (TrackedItem<IMediaFile> item : progress.getFailedItems()) {
                    System.out.println("  File: " + PathUtils.getLocation(item.getItem()));
                    System.out.println("Reason: " + item.getMessage());
                    System.out.println("---------------------------------");
                }
            }
        }
        if (notifySageTV) {
            try {
                if (notifyUrl != null) {
                    System.out.println("Notifying Sage to Refresh Imported Media via " + notifyUrl);
                    URL url = new URL(notifyUrl);
                    URLConnection conn = url.openConnection();
                    HTTPUtils.configueURLConnection(conn, notifyUser, notifyPassword);
                    conn.getContent();
                } else {
                    System.out.println("Notifying Sage to Refresh Imported Media using RMI");
                    proxyAPI.enableRemoteAPI(true);
                    Global.RunLibraryImportScan(false);
                }
            } catch (Throwable t) {
                System.out.println("Failed to notify SageTV: " + t.getMessage());
                log.error("Failed to notify sagetv for update", t);
            }
        }
    }
