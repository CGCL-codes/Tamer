    public boolean containsEntry(Identifier entityRef) {
        if (getItemForIdentifier(entityRef) != null) {
            boolean confirm = MessageDialog.openConfirm(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Overwrite " + getTypeAsText(), getTypeAsText() + " '" + entityRef.toString() + "' already exists in the repository!" + "\nDo you want to overwrite it?");
            if (false == confirm) {
                return true;
            } else {
                removeEntityFromRepository((IRI) entityRef);
                getItemForIdentifier(entityRef).dispose();
            }
        }
        return false;
    }
