        public void elementStateValidationChanged(final Object element, final boolean isStateValidated) {
            if (element != null && element.equals(getEditorInput())) {
                Runnable r = new Runnable() {

                    public void run() {
                        enableSanityChecking(true);
                        if (isStateValidated) {
                            GraphicalViewer viewer = getGraphicalViewer();
                            if (viewer != null) {
                                enableStateValidation(false);
                            }
                        } else {
                            GraphicalViewer viewer = getGraphicalViewer();
                            if (viewer != null) {
                                enableStateValidation(true);
                            }
                        }
                    }
                };
                execute(r, false);
            }
        }
