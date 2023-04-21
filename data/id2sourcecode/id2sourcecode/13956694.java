    private void attemptCloseDialog(List throwables) {
        try {
            if (!throwables.isEmpty()) {
                try {
                    Log.warn("Taking screen capture b/c of failure while testing the " + _title + " dialog.");
                    ScreenCapture.createScreenCapture(FAILURE_TESTING_DIALOG);
                } catch (Throwable t) {
                    Log.log(t);
                }
            }
            doCloseDialog(throwables.isEmpty());
        } catch (Throwable t) {
            Log.log(t);
            throwables.add(t);
        } finally {
            try {
                Robot.wait(new Condition() {

                    public boolean test() {
                        return (_dialogShell != null && _dialogShell.isDisposed());
                    }

                    public String toString() {
                        return _title + " failed to close within " + _timeoutCloseSeconds + " seconds.";
                    }
                }, _timeoutCloseSeconds * 1000, 1000);
            } catch (Throwable t) {
                Log.log(t);
                throwables.add(t);
            } finally {
                if (_dialogShell != null && !_dialogShell.isDisposed()) {
                    Log.warn("trying to brute force close dialog with title: " + _title);
                    try {
                        Log.warn("Taking screen capture b/c of failure while closing the " + _title + " dialog.");
                        ScreenCapture.createScreenCapture(FORCE_CLOSE_DIALOG);
                    } catch (Throwable t) {
                        Log.log(t);
                    }
                    if (_dialogShell.getDisplay() != null) {
                        Robot.syncExec(_dialogShell.getDisplay(), this, new Runnable() {

                            public void run() {
                                if (!_dialogShell.isDisposed()) _dialogShell.close();
                            }
                        });
                    }
                }
            }
        }
    }
