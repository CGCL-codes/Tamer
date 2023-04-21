            @Override
            public void windowClosed(WindowEvent e) {
                if (databaseAdded) {
                    SPObject currentEditor = session.getWorkspace().getEditorPanelModel();
                    try {
                        final URI resource = WabitSwingSessionContextImpl.class.getResource(WabitSessionContext.NEW_WORKSPACE_URL).toURI();
                        URL importURL = resource.toURL();
                        URLConnection urlConnection = importURL.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        final OpenWorkspaceXMLDAO workspaceLoader = new OpenWorkspaceXMLDAO(context, in, urlConnection.getContentLength());
                        workspaceLoader.importWorkspaces(session);
                    } catch (Exception ex) {
                        throw new RuntimeException("Cannot find the templates file at " + "location " + WabitSessionContext.NEW_WORKSPACE_URL);
                    }
                    session.getWorkspace().setEditorPanelModel(currentEditor);
                    context.registerChildSession(session);
                }
                session.getWorkspace().removeDatabaseListChangeListener(workspaceDataSourceListener);
            }
