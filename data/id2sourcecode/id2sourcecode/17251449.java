    private boolean createUserInterface() {
        ComponentId userInterfaceId = ui.getChannel().getComponentId("annone/userinterface/UserInterface");
        MethodId userInterface_newId = userInterfaceId.getMethodId("new()");
        userInterfaceTargetId = userInterface_newId.invoke(null, null, null);
        if (userInterfaceTargetId == null) {
            addError(new Exception(Text.get("User interface not created.")));
            return false;
        }
        return true;
    }
