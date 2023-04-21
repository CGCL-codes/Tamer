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
