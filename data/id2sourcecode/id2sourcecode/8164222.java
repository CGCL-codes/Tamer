    @Override
    public Command createDeleteCommand(List objects) {
        Command command = super.createDeleteCommand(objects);
        if (command == null) {
            return null;
        }
        if (command instanceof CompoundCommand) {
            CompoundCommand compoundCommand = (CompoundCommand) command;
            if (compoundCommand.getCommands().isEmpty()) {
                return null;
            }
        }
        EditPart editPart = part.getGraphicalViewer().getContents();
        ERDiagram diagram = (ERDiagram) editPart.getModel();
        return new WithoutUpdateCommandWrapper(command, diagram);
    }
