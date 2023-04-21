    private void storeResults(final DefaultSimulationResultEater eater, final IFolder resultFolder, final IProgressMonitor monitor) throws CoreException {
        final Object result1 = eater.getResult(ISobekCalculationJobConstants.CALCULATION_RESULT_POINTS);
        final Object result2 = eater.getResult(ISobekCalculationJobConstants.CALCULATION_RESULT_STRUCTURES);
        if (result1 instanceof File) {
            final File file = (File) result1;
            final IFile iFile = resultFolder.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        if (result2 instanceof File) {
            final File file = (File) result2;
            final IFile iFile = resultFolder.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        final IFolder logs = resultFolder.getFolder("logs");
        if (!logs.exists()) logs.create(true, true, monitor);
        final Object log1 = eater.getResult(ISobekCalculationJobConstants.LOG_PI2SOBEK);
        final Object log2 = eater.getResult(ISobekCalculationJobConstants.LOG_OPENMI_CONTROL);
        final Object log3 = eater.getResult(ISobekCalculationJobConstants.LOG_SOBEK);
        final Object log4 = eater.getResult(ISobekCalculationJobConstants.LOG_SOBEK2PI_POINTS);
        final Object log5 = eater.getResult(ISobekCalculationJobConstants.LOG_SOBEK2PI_STRUCTURES);
        if (log1 instanceof File) {
            final File file = (File) log1;
            final IFile iFile = logs.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        if (log2 instanceof File) {
            final File file = (File) log2;
            final IFile iFile = logs.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        if (log3 instanceof File) {
            final File file = (File) log3;
            final IFile iFile = logs.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        if (log4 instanceof File) {
            final File file = (File) log4;
            final IFile iFile = logs.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        if (log5 instanceof File) {
            final File file = (File) log5;
            final IFile iFile = logs.getFile(file.getName());
            try {
                FileUtils.copyFile(file, iFile.getLocation().toFile());
            } catch (final IOException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
        WorkspaceSync.sync(logs, IResource.DEPTH_ONE);
        WorkspaceSync.sync(resultFolder, IResource.DEPTH_ONE);
    }
