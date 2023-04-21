    @Override
    public boolean performFinish() {
        serviceCreator = RpcServiceLayerCreator.createNewRpcServiceLayerCreator();
        serviceCreator.setEntities(configureRPCPage.getEntityTypes());
        serviceCreator.setGaeProjectSrc(configureRPCPage.getContainerRoot());
        serviceCreator.setServiceName(configureRPCPage.getServiceName());
        if (serviceCreator.serviceExists()) {
            boolean answer = MessageDialog.openQuestion(configureRPCPage.getShell(), "RPC Service exists", configureRPCPage.getServiceName() + " already exists. Would you like to overwrite the existing service?");
            if (!answer) {
                return false;
            }
        }
        UpdateQueryBuilder.incrementRPCLayerCount(project, false);
        boolean result = super.performFinish();
        if (result) {
            try {
                JavaUI.openInEditor(serviceCreator.getElement());
            } catch (PartInitException e) {
                AppEngineRPCPlugin.log(e);
            } catch (JavaModelException e) {
                AppEngineRPCPlugin.log(e);
            }
        }
        return result;
    }
