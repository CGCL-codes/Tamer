    public ArrayList<WorkspaceConfig> getWorkspaceConfig() {
        syncCurrentWorkspace();
        ArrayList<WorkspaceConfig> workspaceDatas = new ArrayList<WorkspaceConfig>();
        for (JLabWorkspace currWorkspace : this.workspaces) {
            WorkspaceConfig newWorkspaceData = new WorkspaceConfig();
            newWorkspaceData.setName(currWorkspace.getName());
            ArrayList<FrameConfig> frames = new ArrayList<FrameConfig>();
            ArrayList<ExternalFrameConfig> externalFrames = new ArrayList<ExternalFrameConfig>();
            newWorkspaceData.setFrames(frames);
            newWorkspaceData.setExternalFrames(externalFrames);
            workspaceDatas.add(newWorkspaceData);
            newWorkspaceData.setWindowCount(currWorkspace.getWindowCount());
            for (ModuleWindowInfo currInfo : currWorkspace.getWindowInfos()) {
                FrameConfig newFrameData = new FrameConfig();
                newFrameData.setHeight(currInfo.getHeight());
                newFrameData.setWidth(currInfo.getWidth());
                newFrameData.setPosX(currInfo.getPosX());
                newFrameData.setPosY(currInfo.getPosY());
                newFrameData.setModuleClassName(currInfo.getModule().getClass().getName());
                newFrameData.setModuleId(currInfo.getModule().getId());
                newFrameData.setFrameName(currInfo.getWindowName());
                newFrameData.setParametersAsHashMap(currInfo.getParameters());
                if (currInfo.getModule().getBoard() != null) {
                    newFrameData.setAddress(currInfo.getModule().getBoard().getAddress());
                    newFrameData.setCommChannel(currInfo.getModule().getBoard().getCommChannel().getChannelName());
                } else {
                    newFrameData.setAddress(-1);
                    newFrameData.setCommChannel("-");
                }
                frames.add(newFrameData);
            }
            for (ExternalModuleWindowInfo currInfo : currWorkspace.getExternalWindowInfos()) {
                ExternalFrameConfig newFrameData = new ExternalFrameConfig();
                newFrameData.setHeight(currInfo.getHeight());
                newFrameData.setWidth(currInfo.getWidth());
                newFrameData.setPosX(currInfo.getPosX());
                newFrameData.setPosY(currInfo.getPosY());
                newFrameData.setUiModuleIdentifier(currInfo.getModuleIdentifier());
                newFrameData.setModelIdentifier(currInfo.getModelIdentifier());
                newFrameData.setFrameName(currInfo.getWindowName());
                newFrameData.setParametersAsMap(currInfo.getParameters());
                externalFrames.add(newFrameData);
            }
        }
        return workspaceDatas;
    }
