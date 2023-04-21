    private static void copyExampleResources(final IWorkspaceRoot root, final String source, final IContainer target, final CopyFilesAndFoldersOperation copyOper) {
        try {
            target.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (final CoreException e) {
            XAHelpLogger.logError("Failed to refresh project " + target.getName() + " before initializing with example's files and folders", e);
        }
        String adjustedSource = source;
        if (adjustedSource.startsWith("/")) {
            adjustedSource = adjustedSource.substring(1);
        }
        if (!adjustedSource.endsWith("/")) {
            adjustedSource += "/";
        }
        final File base = new File(adjustedSource);
        if (base.isDirectory()) {
            String copyCandidates[] = base.list();
            final List<String> ccList = new ArrayList<String>(copyCandidates.length);
            for (int i = 0; i < copyCandidates.length; i++) {
                if (!copyCandidates[i].startsWith(".")) {
                    final IResource res = target.findMember(copyCandidates[i]);
                    if (res != null) {
                        if (res instanceof IContainer) {
                            copyExampleResources(root, adjustedSource + copyCandidates[i], (IContainer) res, copyOper);
                        } else if (res instanceof IFile) {
                            ccList.add(adjustedSource + copyCandidates[i]);
                        }
                    } else {
                        ccList.add(adjustedSource + copyCandidates[i]);
                    }
                }
            }
            if (ccList.size() > 0) {
                copyCandidates = new String[ccList.size()];
                copyCandidates = ccList.toArray(copyCandidates);
                copyOper.copyFiles(copyCandidates, target);
            }
        } else if (base.isFile()) {
            final String baseList[] = new String[1];
            baseList[0] = base.getAbsolutePath();
            copyOper.copyFiles(baseList, target);
        }
    }
