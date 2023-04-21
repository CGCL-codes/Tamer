    public boolean passesValidation() {
        File parent = new File(this.parentDirectory.getValue());
        File project = new File(parent, this.projectFilename.getValue());
        if (project.toString().indexOf('.') != -1) {
            EncogWorkBench.displayError("Error", "A project name must not have an extension.");
            return false;
        }
        if (project.exists()) {
            if (!EncogWorkBench.askQuestion("Exists", "A directory with that project name already exists. \nDo you wish to overwrite it?")) return false;
        }
        return true;
    }
