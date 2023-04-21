    protected void assembleRemoteFiles(PluginDescriptor pd, File remoteProductDir) {
        ExtensionPoint remoteFileExnPt = pd.getExtensionPoint(EXNPT_REMOTE_FILE);
        if (remoteFileExnPt != null && getAbstractWARPluginDescriptor().getId().equals(remoteFileExnPt.getParentPluginId())) {
            for (Extension exn : remoteFileExnPt.getConnectedExtensions()) {
                for (Parameter fileParam : exn.getParameters("file")) {
                    String filePath = fileParam.valueAsString();
                    if (filePath == null) {
                        throw new RuntimeException(exn.getUniqueId() + " requires a file parameter value");
                    }
                    File jar = getFilePath(exn.getDeclaringPluginDescriptor(), filePath);
                    try {
                        FileUtils.copyFileToDirectory(jar, remoteProductDir);
                        colocateChecksum(new File(remoteProductDir, jar.getName()));
                    } catch (IOException ex) {
                        throw new RuntimeException("Could not copy the remote file " + jar.getPath() + " to: " + remoteProductDir.getPath(), ex);
                    }
                }
            }
        }
    }
