    public void performAction() {
        Date date = ArchivePrefs.getDate();
        String path = ArchivePrefs.getPath();
        boolean doneProjectsOnly = ArchivePrefs.isDoneProjectsOnly();
        Frame frame = WindowManager.getDefault().getMainWindow();
        ArchiveDialog dialog = new ArchiveDialog(frame, true, date, path, doneProjectsOnly);
        dialog.setVisible(true);
        if (!dialog.archive) {
            LOG.fine("User did not select archive");
            return;
        }
        StatusDisplayer.getDefault().setStatusText(NbBundle.getMessage(ArchiveAction.class, "archiving"));
        archiveDate = DateUtils.getEnd(dialog.getArchiveDate());
        String archivePath = dialog.getArchivePath();
        doneProjectsOnly = dialog.isDoneProjectsOnly();
        DataStore datastore = (DataStore) DataStoreLookup.instance().lookup(DataStore.class);
        if (datastore == null) {
            LOG.severe("Data store could not be obtained.");
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        File dataFile = new File(datastore.getPath());
        if (!dataFile.isFile()) {
            LOG.severe("Data file path error.");
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        File archiveFolder = (archivePath == null || archivePath.equals("")) ? dataFile.getParentFile() : new File(archivePath);
        if (!archiveFolder.isDirectory()) {
            LOG.severe("Archive directory error.");
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        String filename = UtilsFile.removeExtension(dataFile.getName());
        String extension = UtilsFile.getExtension(dataFile.getName());
        Date currentDate = Calendar.getInstance().getTime();
        String currentDateStamp = DATESTAMP.format(currentDate);
        String currentTimeStamp = TIMESTAMP.format(currentDate);
        File backupFile = new File(archiveFolder, filename + "-" + currentDateStamp + "-" + currentTimeStamp + ".backup." + extension);
        try {
            UtilsFile.copyFile(dataFile, backupFile);
        } catch (Exception ex) {
            LOG.severe("Error creating archive backup of datafile. " + ex.getMessage());
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        String archiveDateStamp = DATESTAMP.format(archiveDate);
        File archiveFile = new File(archiveFolder, filename + "-" + archiveDateStamp + "-" + currentTimeStamp + ".archive." + extension);
        try {
            UtilsFile.copyFile(dataFile, archiveFile);
        } catch (Exception ex) {
            LOG.severe("Error creating archive copy of datafile. " + ex.getMessage());
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        archiveSingleActions = new Vector<Action>();
        archiveActions = new Vector<Action>();
        archiveProjects = new Vector<Project>();
        Data archiveData = null;
        try {
            archiveData = XStreamWrapper.instance().load(archiveFile);
        } catch (Exception ex) {
            LOG.severe("Error loading data from archive file. " + ex.getMessage());
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        for (Action action : archiveData.getRootActions().getChildren(Action.class)) {
            if (action.isDone() && action.getDoneDate().before(archiveDate)) {
                archiveSingleActions.add(action);
            } else {
                LOG.fine("Removing from archive - action: " + action.getDescription());
                action.removeFromParent();
            }
        }
        if (doneProjectsOnly) {
            for (Project project : archiveData.getRootProjects().getChildren(Project.class)) {
                if (project.isDone() && project.getDoneDate().before(archiveDate)) {
                    archiveProjects.add(project);
                } else {
                    LOG.fine("Removing from archive - project: " + project.getDescription());
                    project.removeFromParent();
                }
            }
        } else {
            for (Project project : archiveData.getRootProjects().getChildren(Project.class)) {
                keepArchived(project);
            }
        }
        Manager<Thought> archiveThoughtManager = archiveData.getThoughtManager();
        for (Thought thought : archiveThoughtManager.list()) {
            if (!thought.isProcessed()) {
                archiveThoughtManager.remove(thought);
            }
        }
        for (Iterator<Project> i = archiveData.getRootFutures().iterator(Project.class); i.hasNext(); ) {
            i.next().removeFromParent();
        }
        for (Iterator<Project> i = archiveData.getRootTemplates().iterator(Project.class); i.hasNext(); ) {
            i.next().removeFromParent();
        }
        archiveData.getFutureManager().removeAll();
        archiveData.getInformationManager().removeAll();
        RecurrenceRemover.removeAll(archiveData);
        try {
            XStreamWrapper.instance().store(archiveData, archiveFile);
        } catch (Exception ex) {
            LOG.severe("Error storing archive. " + ex.getMessage());
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data could not be obtained.");
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }
        Project singleActions = data.getRootActions();
        for (Action a : archiveSingleActions) {
            LOG.fine("Removing from data - single action: " + a.getDescription());
            singleActions.remove(a);
        }
        Map<Integer, Project> dataProjectsMap = createProjectsMap(data);
        for (Action archiveAction : archiveActions) {
            LOG.fine("Removing from data file - archived action: " + archiveAction.getDescription());
            Project dataParent = dataProjectsMap.get(archiveAction.getParent().getID());
            if (dataParent != null) {
                if (!dataParent.remove(archiveAction)) {
                    LOG.severe("Archived action could not be removed from data file.");
                }
            } else {
                LOG.severe("Could not find parent project in data file for archived action.");
            }
        }
        for (Project archiveProject : archiveProjects) {
            LOG.fine("Removing from data file - archived project: " + archiveProject.getDescription());
            Project dataParent = dataProjectsMap.get(archiveProject.getParent().getID());
            if (dataParent != null) {
                if (!dataParent.remove(archiveProject)) {
                    LOG.severe("Archived project could not be removed from data file.");
                }
            } else {
                LOG.severe("Could not find parent project in data file for archived project.");
            }
        }
        saveData(datastore);
        ArchivePrefs.setDate(archiveDate);
        ArchivePrefs.setPath(archivePath);
        ArchivePrefs.setDoneProjectsOnly(doneProjectsOnly);
        StatusDisplayer.getDefault().setStatusText("");
        String t = Constants.TITLE + " " + NbBundle.getMessage(getClass(), "CTL_ArchiveAction");
        String COMPLETED = NbBundle.getMessage(getClass(), "archive.completed");
        String ARCHIVE_DATE = NbBundle.getMessage(getClass(), "archive.date");
        String BACKUP_FILE = NbBundle.getMessage(getClass(), "backup.file");
        String ARCHIVE_FILE = NbBundle.getMessage(getClass(), "archive.file");
        String SINGLE_ACTIONS = NbBundle.getMessage(getClass(), "single.actions");
        String PROJECTS = NbBundle.getMessage(getClass(), "projects");
        String PROJECT_ACTIONS = NbBundle.getMessage(getClass(), "project.actions");
        String m = COMPLETED + ". \n\n" + ARCHIVE_DATE + ": " + archiveDate + "\n\n" + BACKUP_FILE + ": " + backupFile.getPath() + "\n" + ARCHIVE_FILE + ": " + archiveFile.getPath() + "\n\n";
        JOptionPane.showMessageDialog(frame, m, t, JOptionPane.INFORMATION_MESSAGE);
    }
