    public synchronized void put(TaskDataState taskDataState) {
        FileOutputStream fileOutputStream = null;
        FileLock lock = null;
        try {
            String repositoryFolder = URLEncoder.encode(taskDataState.getNewTaskData().getRepositoryUrl(), ENCODING_UTF_8);
            File dataFile = getDataFile(repositoryFolder, taskDataState.getId());
            if (dataFile != null) {
                if (!dataFile.getParentFile().exists()) {
                    if (!dataFile.getParentFile().mkdirs()) {
                        throw new IOException("Could not create offline data folder: " + dataFile.getParentFile().getAbsolutePath());
                    }
                }
                fileOutputStream = new FileOutputStream(dataFile);
                FileChannel channel = fileOutputStream.getChannel();
                lock = channel.tryLock();
                if (lock != null) {
                    final ZipOutputStream outputStream = new ZipOutputStream(fileOutputStream);
                    outputStream.setMethod(ZipOutputStream.DEFLATED);
                    ZipEntry zipEntry = new ZipEntry(FILE_NAME_INTERNAL);
                    outputStream.putNextEntry(zipEntry);
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream, ENCODING_UTF_8);
                    XMLMemento memento = XMLMemento.createWriteRoot(ELEMENT_TASK_STATE);
                    memento.putString(ATTRIBUTE_VERSION, SCHEMA_VERSION);
                    if (taskDataState.getNewTaskData() != null) {
                        IMemento child = memento.createChild(ELEMENT_NEW_DATA);
                        addTaskData(child, taskDataState.getNewTaskData());
                    }
                    if (taskDataState.getOldTaskData() != null) {
                        IMemento child = memento.createChild(ELEMENT_OLD_DATA);
                        addTaskData(child, taskDataState.getOldTaskData());
                    }
                    if (taskDataState.getEdits() != null && taskDataState.getEdits().size() > 0) {
                        IMemento child = memento.createChild(ELEMENT_EDITS_DATA);
                        addEdits(child, taskDataState.getEdits());
                    }
                    memento.save(writer);
                }
            }
        } catch (Exception e) {
            StatusHandler.fail(e, "Error saving offline data", false);
        } finally {
            try {
                if (lock != null && lock.isValid()) {
                    lock.release();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                StatusHandler.fail(e, "Error closing offline data output stream", false);
            }
        }
    }
