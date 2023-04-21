    @Override
    @SuppressWarnings("deprecation")
    public IStatus run(final IProgressMonitor progressMonitor) {
        progressMonitor.beginTask(JOB_LABEL, IProgressMonitor.UNKNOWN);
        final File monitorFile = UiUsageMonitorPlugin.getDefault().getMonitorLogFile();
        InteractionEventLogger logger = UiUsageMonitorPlugin.getDefault().getInteractionLogger();
        logger.stopMonitoring();
        List<InteractionEvent> events = logger.getHistoryFromFile(monitorFile);
        progressMonitor.worked(1);
        int nowMonth = Calendar.getInstance().get(Calendar.MONTH);
        if (events.size() > 0 && events.get(0).getDate().getMonth() != nowMonth) {
            int currMonth = events.get(0).getDate().getMonth();
            String fileName = getYear(events.get(0)) + "-" + getMonth(currMonth) + "-" + BACKUP_FILE_SUFFIX;
            File dir = new File(getZippedMonitorFileDirPath());
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                File currBackupZipFile = new File(dir, fileName + ZIP_EXTENSION);
                if (!currBackupZipFile.exists()) {
                    currBackupZipFile.createNewFile();
                }
                ZipOutputStream zipFileStream;
                zipFileStream = new ZipOutputStream(new FileOutputStream(currBackupZipFile));
                zipFileStream.putNextEntry(new ZipEntry(UiUsageMonitorPlugin.getDefault().getMonitorLogFile().getName()));
                for (InteractionEvent event : events) {
                    int monthOfCurrEvent = event.getDate().getMonth();
                    if (monthOfCurrEvent == currMonth) {
                        String xml = logger.writeLegacyEvent(event);
                        zipFileStream.write(xml.getBytes());
                    } else if (monthOfCurrEvent != nowMonth) {
                        progressMonitor.worked(1);
                        zipFileStream.closeEntry();
                        zipFileStream.close();
                        fileName = getYear(event) + "-" + getMonth(monthOfCurrEvent) + "-" + BACKUP_FILE_SUFFIX;
                        currBackupZipFile = new File(dir, fileName + ZIP_EXTENSION);
                        if (!currBackupZipFile.exists()) {
                            currBackupZipFile.createNewFile();
                        }
                        zipFileStream = new ZipOutputStream(new FileOutputStream(currBackupZipFile));
                        zipFileStream.putNextEntry(new ZipEntry(UiUsageMonitorPlugin.getDefault().getMonitorLogFile().getName()));
                        currMonth = monthOfCurrEvent;
                        String xml = logger.writeLegacyEvent(event);
                        zipFileStream.write(xml.getBytes());
                    } else if (monthOfCurrEvent == nowMonth) {
                        logger.clearInteractionHistory(false);
                        logger.interactionObserved(event);
                    }
                }
                zipFileStream.closeEntry();
                zipFileStream.close();
            } catch (FileNotFoundException e) {
                StatusHandler.log("Mylyn monitor log rollover failed - " + e.getMessage(), this);
            } catch (IOException e) {
                StatusHandler.log("Mylyn monitor log rollover failed - " + e.getMessage(), this);
            }
        }
        progressMonitor.worked(1);
        logger.startMonitoring();
        generator = new ReportGenerator(UiUsageMonitorPlugin.getDefault().getInteractionLogger(), collectors, this, forceSyncForTesting);
        progressMonitor.worked(1);
        final List<File> files = new ArrayList<File>();
        files.add(monitorFile);
        input = new UsageStatsEditorInput(files, generator);
        progressMonitor.done();
        if (forceSyncForTesting) {
            try {
                final IEditorInput input = this.input;
                IWorkbenchPage page = UiUsageMonitorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
                if (page == null) {
                    return new Status(Status.ERROR, UiUsageMonitorPlugin.PLUGIN_ID, Status.OK, "Mylyn Usage Summary", null);
                }
                if (input != null) {
                    page.openEditor(input, UsageSummaryReportEditorPart.ID);
                }
            } catch (PartInitException e1) {
                StatusHandler.fail(e1, "Could not show usage summary", true);
            }
        }
        return Status.OK_STATUS;
    }
