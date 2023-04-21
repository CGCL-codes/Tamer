    protected void execute(Event event) {
        GraphicalViewer viewer = this.getGraphicalViewer();
        List<Command> commandList = new ArrayList<Command>();
        for (Object object : viewer.getSelectedEditParts()) {
            List<Command> subCommandList = this.getCommand((EditPart) object, event);
            commandList.addAll(subCommandList);
        }
        if (!commandList.isEmpty()) {
            CompoundCommand compoundCommand = new CompoundCommand();
            for (Command command : commandList) {
                compoundCommand.add(command);
            }
            this.execute(compoundCommand);
        }
    }
