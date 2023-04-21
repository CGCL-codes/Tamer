    public static void loadFiles(final WabitSwingSessionContext context, final URI... importFiles) {
        final List<InputStream> ins = new ArrayList<InputStream>();
        final Map<URI, OpenWorkspaceXMLDAO> workspaceLoaders = new HashMap<URI, OpenWorkspaceXMLDAO>();
        List<String> invalidWorkspaces = new ArrayList<String>();
        for (URI importFile : importFiles) {
            BufferedInputStream in = null;
            OpenWorkspaceXMLDAO workspaceLoader = null;
            try {
                URL importURL = importFile.toURL();
                URLConnection urlConnection = importURL.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                ins.add(in);
                workspaceLoader = new OpenWorkspaceXMLDAO(context, in, urlConnection.getContentLength());
                workspaceLoaders.put(importFile, workspaceLoader);
            } catch (Exception e) {
                logger.info("Can't deal with URI " + importFile, e);
                StringBuilder message = new StringBuilder();
                message.append(importFile.toString());
                if (e instanceof FileNotFoundException) {
                    message.append(" (File not found)");
                } else {
                    message.append(" (").append(e.toString()).append(")");
                }
                invalidWorkspaces.add(message.toString());
            }
        }
        if (!invalidWorkspaces.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("The following workspace locations cannot be opened:\n");
            for (String s : invalidWorkspaces) {
                message.append("\n").append(s);
            }
            JOptionPane.showMessageDialog(context.getFrame(), message.toString(), "Some workspaces not opened", JOptionPane.WARNING_MESSAGE);
        }
        SPSwingWorker worker = new SPSwingWorker(context.getLoadingRegistry()) {

            private volatile OpenWorkspaceXMLDAO currentDAO;

            @Override
            public void doStuff() throws Exception {
                for (OpenWorkspaceXMLDAO dao : workspaceLoaders.values()) {
                    currentDAO = dao;
                    dao.loadWorkspacesFromStream();
                }
            }

            @Override
            public void cleanup() throws Exception {
                for (InputStream in : ins) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close a workspace input stream. " + "Squishing this exception: " + e);
                    }
                }
                if (getDoStuffException() != null) {
                    SPSUtils.showExceptionDialogNoReport(context.getFrame(), "Wabit had trouble opening your workspace", getDoStuffException());
                    return;
                }
                if (!isCancelled()) {
                    for (Map.Entry<URI, OpenWorkspaceXMLDAO> entry : workspaceLoaders.entrySet()) {
                        WabitSession registeredSession = null;
                        try {
                            registeredSession = entry.getValue().addLoadedWorkspacesToContext();
                            if (registeredSession != null) {
                                ((WabitSwingSession) registeredSession).setCurrentURI(entry.getKey());
                            }
                            try {
                                context.putRecentFileName(new File(entry.getKey()).getAbsolutePath());
                            } catch (IllegalArgumentException ignored) {
                            }
                        } catch (Exception e) {
                            SPSUtils.showExceptionDialogNoReport(context.getFrame(), "Wabit had trouble after opening the workspace located at " + entry.getKey(), e);
                        }
                    }
                    context.setEditorPanel();
                }
            }

            @Override
            protected Integer getJobSizeImpl() {
                int jobSize = 0;
                for (OpenWorkspaceXMLDAO workspaceDAO : workspaceLoaders.values()) {
                    jobSize += workspaceDAO.getJobSize();
                }
                return jobSize;
            }

            @Override
            protected String getMessageImpl() {
                OpenWorkspaceXMLDAO myCurrentDAO = currentDAO;
                if (myCurrentDAO != null) {
                    return myCurrentDAO.getMessage();
                } else {
                    return "";
                }
            }

            @Override
            protected int getProgressImpl() {
                int progress = 0;
                for (OpenWorkspaceXMLDAO workspaceDAO : workspaceLoaders.values()) {
                    progress += workspaceDAO.getProgress();
                }
                return progress;
            }

            @Override
            protected boolean hasStartedImpl() {
                boolean started = false;
                for (OpenWorkspaceXMLDAO workspaceDAO : workspaceLoaders.values()) {
                    started = started || workspaceDAO.hasStarted();
                }
                return started;
            }

            @Override
            protected boolean isFinishedImpl() {
                if (getDoStuffException() != null) {
                    return true;
                }
                boolean finished = true;
                for (OpenWorkspaceXMLDAO workspaceDAO : workspaceLoaders.values()) {
                    finished = finished && workspaceDAO.isFinished();
                }
                return finished;
            }

            @Override
            public synchronized boolean isCancelled() {
                boolean cancelled = false;
                for (OpenWorkspaceXMLDAO workspaceDAO : workspaceLoaders.values()) {
                    cancelled = cancelled || workspaceDAO.isCancelled();
                }
                return cancelled;
            }

            @Override
            public synchronized void setCancelled(boolean cancelled) {
                for (OpenWorkspaceXMLDAO workspaceDAO : workspaceLoaders.values()) {
                    workspaceDAO.setCancelled(cancelled);
                }
            }
        };
        OpenProgressWindow.showProgressWindow(context.getFrame(), worker);
        new Thread(worker).start();
    }
