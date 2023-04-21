    private void execute(final Runnable readAction, final Runnable writeAction) {
        ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable() {

            public void run() {
                readAction.run();
                new WriteCommandAction(myProject, title, null) {

                    protected void run(Result result) throws Throwable {
                        writeAction.run();
                    }
                }.execute();
            }
        }, title, true, myProject);
    }
