    public void doImport(java.io.File f, OverwriteMode mode) throws ImportArchiveException {
        File archive = new File(f);
        String archivePath = f.getAbsolutePath();
        File dbDir = new File(archivePath + "/db");
        Session session = null;
        try {
            session = DBManager.getInstance().createSession();
            session.beginTransaction();
            String[] files = dbDir.list(new DBFilter());
            if (files != null && files.length > 0) {
                FileInputStream fo = null;
                try {
                    fo = new FileInputStream(archivePath + "/db/" + files[0]);
                    SafiDriverManager loadedManager = loadManager(fo);
                    if (loadedManager != null) {
                        SafiDriverManager manager = DBManager.getInstance().getDriverManagerFromDB(session);
                        List<DBDriver> drivers = new ArrayList<DBDriver>(loadedManager.getDrivers());
                        for (DBDriver d : drivers) {
                            DBDriver existingDriver = manager.getDriver(d.getName());
                            if (existingDriver == null) {
                                manager.getDrivers().add(d);
                                session.saveOrUpdate(manager);
                            } else {
                                log.warn("Driver " + existingDriver.getName() + " already exists");
                                List<DBConnection> connections = new ArrayList<DBConnection>(d.getConnections());
                                for (DBConnection conn : connections) {
                                    DBConnection existingConnection = existingDriver.getConnection(conn.getName());
                                    if (existingConnection == null) {
                                        existingDriver.getConnections().add(conn);
                                        session.saveOrUpdate(existingDriver);
                                    } else {
                                        if (mode == OverwriteMode.FAIL) throw new ImportArchiveException(ImportArchiveException.Type.RESOURCE_EXISTS, "Connection " + existingConnection.getName() + " already exists for driver " + existingDriver.getName());
                                        log.error("Connection " + existingConnection.getName() + " already exists for driver " + existingDriver.getName());
                                        if (mode == OverwriteMode.SKIP) {
                                            List<Query> queries = new ArrayList<Query>(conn.getQueries());
                                            for (Query q : queries) {
                                                Query existingQuery = existingConnection.getQuery(q.getName());
                                                if (existingQuery == null) {
                                                    existingConnection.getQueries().add(q);
                                                    session.saveOrUpdate(existingConnection);
                                                } else {
                                                    log.warn("Query " + q.getName() + " already exists for connection " + existingConnection.getName());
                                                }
                                            }
                                        } else {
                                            existingDriver.getConnections().remove(existingConnection);
                                            session.delete(existingConnection);
                                            existingDriver.getConnections().add(conn);
                                            session.save(existingDriver);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    if (fo != null) try {
                        fo.close();
                    } catch (IOException e) {
                    }
                }
            }
            {
                File globalDir = new File(archivePath + "/globals");
                if (globalDir.exists()) {
                    files = globalDir.list(new GlobalVarFilter());
                    if (files != null && files.length > 0) {
                        for (String file : files) {
                            FileInputStream fo = new FileInputStream(archivePath + "/globals/" + file);
                            Variable v = loadVariable(fo);
                            if (v == null) continue;
                            Variable oldVar = GlobalVariableManager.getInstance().getGlobalVariable(v.getName(), true);
                            if (oldVar != null) {
                                if (mode == OverwriteMode.OVERWRITE) GlobalVariableManager.getInstance().deleteGlobalVariable(oldVar); else if (mode == OverwriteMode.FAIL) throw new ImportArchiveException(ImportArchiveException.Type.RESOURCE_EXISTS, "Global Variable named " + oldVar.getName() + " already exists on this server"); else if (mode == OverwriteMode.SKIP) continue;
                            }
                            GlobalVariableManager.getInstance().addGlobalVariable(v);
                        }
                    }
                }
            }
            File prjDir = new File(archivePath + "/projects");
            files = prjDir.list(new PrjFilter());
            if (files != null && files.length > 0) {
                FileInputStream fo = null;
                for (String file : files) {
                    try {
                        fo = new FileInputStream(archivePath + "/projects/" + file);
                        List<SafletProject> projects = loadProjects(fo);
                        for (SafletProject sp : projects) {
                            SafletProject existingProject = DBManager.getInstance().getSafletProject(session, sp.getName());
                            if (existingProject == null) {
                                DBManager.getInstance().saveOrUpdateServerResource(session, sp);
                            } else {
                                if (mode == OverwriteMode.OVERWRITE) {
                                    session.delete(existingProject);
                                    DBManager.getInstance().saveOrUpdateServerResource(session, sp);
                                } else if (mode == OverwriteMode.FAIL) throw new ImportArchiveException(ImportArchiveException.Type.RESOURCE_EXISTS, "SafletProject named " + sp.getName() + " already exists on this server"); else {
                                    boolean saveProject = false;
                                    List<Saflet> saflets = new ArrayList<Saflet>(sp.getSaflets());
                                    for (Saflet s : saflets) {
                                        if (!hasSaflet(existingProject, s.getName())) {
                                            existingProject.getSaflets().add(s);
                                            saveProject = true;
                                        } else {
                                            log.warn("Project " + existingProject.getName() + " alread contains Saflet named " + s.getName() + ". Import failed");
                                        }
                                    }
                                    if (saveProject) DBManager.getInstance().saveOrUpdateServerResource(session, existingProject);
                                }
                            }
                        }
                    } finally {
                        if (fo != null) try {
                            fo.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            File promptDir = new File(archivePath + "/prompts");
            files = promptDir.list(new PromptFilter());
            if (files != null && files.length > 0) {
                for (String file : files) {
                    FileInputStream fo = null;
                    try {
                        fo = new FileInputStream(archivePath + "/prompts/" + file);
                        Prompt prompt = loadPrompt(fo);
                        if (prompt != null) {
                            Prompt existingPrompt = DBManager.getInstance().getPromptByName(session, prompt.getName());
                            if (existingPrompt != null && mode == OverwriteMode.FAIL) {
                                throw new ImportArchiveException(ImportArchiveException.Type.RESOURCE_EXISTS, "Prompt " + existingPrompt.getName() + " already exists. Import from " + archive + " failed");
                            } else {
                                if (existingPrompt != null && mode == OverwriteMode.OVERWRITE) {
                                    log.warn("Prompt " + existingPrompt.getName() + " already exists. overwriting");
                                    DBManager.getInstance().delete(session, existingPrompt);
                                }
                                prompt.setId(-1);
                                if (prompt.getProject() != null) {
                                    String pname = prompt.getProject().getName();
                                    SafletProject sp = DBManager.getInstance().getSafletProject(session, pname);
                                    sp.getPrompts().add(prompt);
                                    DBManager.getInstance().saveOrUpdateServerResource(session, prompt);
                                    DBManager.getInstance().saveOrUpdateServerResource(session, sp);
                                } else {
                                    DBManager.getInstance().saveOrUpdateServerResource(session, prompt);
                                }
                            }
                        }
                    } finally {
                        if (fo != null) try {
                            fo.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
            {
                String prefix = archivePath + "/prompts";
                File audioDir = new File(prefix);
                String audioDirRoot = SafletEngine.getInstance().getAudioDirectoryRoot();
                if (!audioDirRoot.endsWith("/")) audioDirRoot += "/";
                String initialPath = archivePath;
                if (!initialPath.endsWith("/")) initialPath += "/";
                if (mode == OverwriteMode.FAIL) {
                    String filename = filesExist(initialPath, "prompts/audio/", "", audioDirRoot, false);
                    if (filename != null) throw new ImportArchiveException(ImportArchiveException.Type.RESOURCE_EXISTS, "AudioFile " + filename + " already exists. Import from " + archive + " failed");
                    audioDir.copyTo(new File(audioDirRoot));
                } else if (mode == OverwriteMode.OVERWRITE) {
                    audioDir.copyTo(new File(audioDirRoot));
                } else if (mode == OverwriteMode.SKIP) {
                    filesExist(initialPath, "prompts/audio/", "", audioDirRoot, true);
                }
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Exception caught while importing archive ", e);
            if (session != null && session.getTransaction() != null) session.getTransaction().rollback();
            if (e instanceof ImportArchiveException) throw (ImportArchiveException) e;
            throw new ImportArchiveException(ImportArchiveException.Type.SYSTEM, "Exception caught while importing archive " + archive, e);
        } finally {
            try {
                File.umount(archive, true);
            } catch (ArchiveException e) {
                e.printStackTrace();
            }
            try {
                if (session != null) session.close();
            } catch (Exception e2) {
            }
        }
    }
