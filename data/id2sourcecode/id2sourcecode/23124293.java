    private void doUploadService() throws Exception {
        String endpoint = this.endpointField.getText().trim();
        if (endpoint.length() == 0) {
            throw new Exception("No portal service endpoint specified!");
        }
        Activator.getDefault().getPreferenceStore().setValue(PORTAL_ENDPOINT_PROP, endpoint);
        GoalTreeRepositoryStub stub = new GoalTreeRepositoryStub(endpoint);
        ListRegisteredTreeIDsResponse response = stub.listRegisteredTreeIDs();
        boolean hasToReplace = false;
        if (response != null && response.get_return() != null) {
            for (String id : response.get_return()) {
                if (id.equals(this.ontoID)) {
                    if (MessageDialog.openConfirm(this.shell, "Goal Tree Overwrite?", "Goal Tree with id '" + id + "' already exists in the Portal repository." + "\n\nPlease confirm overwriting!")) {
                        hasToReplace = true;
                        break;
                    } else {
                        return;
                    }
                }
            }
        }
        if (hasToReplace) {
            UnregisterGoalTree arg = new UnregisterGoalTree();
            arg.setParam0(this.ontoID);
            stub.unregisterGoalTree(arg);
        }
        RegisterGoalTree args = new RegisterGoalTree();
        args.setParam0(this.wsmlData);
        args.setParam1(this.xmlData);
        stub.registerGoalTree(args);
        MessageDialog.openInformation(this.shell, "Result", "Goal Tree successfully uploaded!");
        shell.dispose();
    }
