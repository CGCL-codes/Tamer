    public void endUpdate(Execution exec, AuWriter out) throws IOException {
        boolean cleaned = false;
        final Desktop desktop = exec.getDesktop();
        final DesktopCtrl desktopCtrl = (DesktopCtrl) desktop;
        final Configuration config = desktop.getWebApp().getConfiguration();
        final ExecutionCtrl execCtrl = (ExecutionCtrl) exec;
        final UiVisualizer uv = (UiVisualizer) execCtrl.getVisualizer();
        try {
            Event event = nextEvent(uv);
            do {
                for (; event != null; event = nextEvent(uv)) process(desktop, event);
                resumeAll(desktop, uv, null);
            } while ((event = nextEvent(uv)) != null);
            List responses = uv.getResponses();
            final int resId = desktopCtrl.getResponseId(true);
            desktopCtrl.responseSent(out.getChannel(), execCtrl.getRequestId(), new Object[] { new Integer(resId), responses });
            out.writeResponseId(resId);
            out.write(responses);
            cleaned = true;
            desktopCtrl.invokeExecutionCleanups(exec, null, null);
            config.invokeExecutionCleanups(exec, null, null);
        } finally {
            if (!cleaned) {
                desktopCtrl.invokeExecutionCleanups(exec, null, null);
                config.invokeExecutionCleanups(exec, null, null);
            }
            doDeactivate(exec);
        }
    }
