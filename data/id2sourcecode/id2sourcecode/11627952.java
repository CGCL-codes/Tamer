    public void resetWorkspace(final IUIContext ui, final ResetContext context) {
        IWorkbenchHelper workbench = EclipseHelperFactory.getWorkbenchHelper();
        workbench.saveAndWait(ui);
        workbench.waitForBuild(ui);
        ui.wait(new JobExistsCondition(null), 120000, 1000);
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        if (root == null) {
            return;
        }
        final IProject[] allProjects = root.getProjects();
        if ((allProjects == null) || (allProjects.length == 0)) {
            return;
        }
        File[] projectLocations = new File[allProjects.length];
        for (int i = 0; i < allProjects.length; i++) {
            projectLocations[i] = allProjects[i].getLocation().toFile();
        }
        try {
            IResourceHelper resources = EclipseHelperFactory.getResourceHelper();
            resources.createZipCopy(ui, context.getTestClassName(), projectLocations, _fileFilter);
        } catch (Throwable t) {
            ScreenCapture.createScreenCapture(context.getTestClassName() + "_ProjectResetDaemon");
            PlatformActivator.logException(t);
        } finally {
            IWorkspaceRunnable noResourceChangedEventsRunner = new IWorkspaceRunnable() {

                public void run(IProgressMonitor runnerMonitor) throws CoreException {
                    CoreException lastCE = null;
                    for (IProject nextProject : allProjects) {
                        try {
                            nextProject.close(runnerMonitor);
                        } catch (CoreException ce) {
                            PlatformActivator.logException(ce);
                            lastCE = ce;
                        }
                    }
                    if (lastCE != null) {
                        throw lastCE;
                    }
                }
            };
            boolean success = false;
            int retry = 0;
            while (!success && (retry < 5)) {
                try {
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    workspace.run(noResourceChangedEventsRunner, workspace.getRoot(), IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
                    success = true;
                } catch (CoreException ce) {
                    ui.pause(2000);
                    retry++;
                }
            }
            ui.pause(5000);
            workbench.waitForBuild(ui);
            ui.wait(new JobExistsCondition(null), 120000, 1000);
            noResourceChangedEventsRunner = new IWorkspaceRunnable() {

                public void run(IProgressMonitor runnerMonitor) throws CoreException {
                    CoreException lastCE = null;
                    for (IProject nextProject : allProjects) {
                        if (nextProject.exists()) {
                            try {
                                nextProject.delete(true, true, null);
                            } catch (CoreException ce) {
                                PlatformActivator.logException(ce);
                                lastCE = ce;
                            }
                        }
                    }
                    if (lastCE != null) {
                        throw lastCE;
                    }
                }
            };
            success = false;
            retry = 0;
            while (!success && (retry < 5)) {
                try {
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    workspace.run(noResourceChangedEventsRunner, workspace.getRoot(), IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
                    success = true;
                } catch (CoreException ce) {
                    ui.pause(2000);
                    retry++;
                }
            }
            ui.pause(3000);
            workbench.waitForBuild(ui);
            ui.wait(new JobExistsCondition(null), 120000, 1000);
        }
    }
