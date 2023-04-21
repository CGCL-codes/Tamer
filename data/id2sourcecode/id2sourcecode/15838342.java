    private void resetWorkspace(String testClassName) {
        List<IResetDaemon> resetDaemons = ResetDaemonRegistry.getResetDeamons();
        CoreActivator.logDebug("-CALLING (" + resetDaemons.size() + ") IResetDaemons");
        ResetContext context = new ResetContext(testClassName);
        IUIContext ui = getUI();
        for (IResetDaemon nextDaemon : resetDaemons) {
            CoreActivator.logDebug("-CALLING IResetDaemon :" + nextDaemon.getClass().getName());
            try {
                nextDaemon.resetWorkspace(ui, context);
            } catch (Throwable throwable) {
                ScreenCapture.createScreenCapture("RESET_DAEMON_FAILURE__" + nextDaemon.getClass().getCanonicalName());
                CoreActivator.logException(throwable);
            }
        }
    }
