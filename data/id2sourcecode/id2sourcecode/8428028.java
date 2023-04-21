    private void dialogChanged() {
        if (_contextSelection.getText().length() < 1) {
            if (AutoContextRegistration.getInstance().getAll().isEmpty()) {
                setErrorState("No context available. Make sure there's" + " at least one open connection!");
            } else {
                setErrorState("Context needs to be selected.");
            }
        } else if (_resultTarget == null || _resultTarget.length() == 0) {
            setMessage("");
            setErrorState("No target defined");
        } else {
            setErrorState(null);
            setMessage("");
            if (targetExists()) {
                setMessage("Attention: The target exists already on the filesystem.\n" + "Running the automation might overwrite existing data!");
            } else {
                setMessage("");
            }
            super.setPageComplete(true);
        }
    }
