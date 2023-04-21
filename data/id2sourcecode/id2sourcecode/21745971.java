    private void buildChmodTask(Vector chmods) {
        for (int i = 0; i < chmods.size(); i++) {
            FileSystemOperation fileOp = (FileSystemOperation) chmods.elementAt(i);
            String permissions = fileOp.getDestinationName();
            boolean read = permissions.indexOf("r") >= 0;
            boolean write = permissions.indexOf("w") >= 0;
            boolean execute = permissions.indexOf("x") >= 0;
            ChangePermissions chmod = new ChangePermissions(container.getName() + "_CHANGE_PERMISSIONS_" + fileOp + ResourceManager.getNextObjectIdentifier(), new ResourceSet((Storage) fileOp.getStorageItem()), fileOp.getSourceName(), read, write, execute);
            actionGroup.add(chmod);
            fileOp.setAbstractAction(chmod);
        }
    }
