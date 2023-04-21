    public void execUpdate(Execution exec, List requests, AuWriter out) throws IOException {
        if (requests == null) throw new IllegalArgumentException();
        assert D.OFF || ExecutionsCtrl.getCurrentCtrl() == null : "Impossible to re-activate for update: old=" + ExecutionsCtrl.getCurrentCtrl() + ", new=" + exec;
        final Desktop desktop = exec.getDesktop();
        final DesktopCtrl desktopCtrl = (DesktopCtrl) desktop;
        final Configuration config = desktop.getWebApp().getConfiguration();
        final PerformanceMeter pfmeter = config.getPerformanceMeter();
        long startTime = 0;
        if (pfmeter != null) {
            startTime = System.currentTimeMillis();
            meterAuClientComplete(pfmeter, exec);
        }
        final UiVisualizer uv = doActivate(exec, true, false);
        final String sid = ((ExecutionCtrl) exec).getRequestId();
        if (sid != null && isReqDup0(exec, out, sid)) {
            doDeactivate(exec);
            return;
        }
        final Monitor monitor = config.getMonitor();
        if (monitor != null) {
            try {
                monitor.beforeUpdate(desktop, requests);
            } catch (Throwable ex) {
                log.error(ex);
            }
        }
        final String pfReqId = pfmeter != null ? meterAuStart(pfmeter, exec, startTime) : null;
        Collection doneReqIds = null;
        AbortingReason abrn = null;
        boolean cleaned = false;
        try {
            final RequestQueue rque = desktopCtrl.getRequestQueue();
            rque.addRequests(requests);
            config.invokeExecutionInits(exec, null);
            desktopCtrl.invokeExecutionInits(exec, null);
            if (pfReqId != null) rque.addPerfRequestId(pfReqId);
            final List errs = new LinkedList();
            final long tmexpired = System.currentTimeMillis() + config.getMaxProcessTime();
            for (AuRequest request; System.currentTimeMillis() < tmexpired && (request = rque.nextRequest()) != null; ) {
                try {
                    process(exec, request, !errs.isEmpty());
                } catch (ComponentNotFoundException ex) {
                } catch (Throwable ex) {
                    handleError(ex, uv, errs);
                }
                Event event = nextEvent(uv);
                do {
                    for (; event != null; event = nextEvent(uv)) {
                        try {
                            process(desktop, event);
                        } catch (Throwable ex) {
                            handleError(ex, uv, errs);
                        }
                    }
                    resumeAll(desktop, uv, errs);
                } while ((event = nextEvent(uv)) != null);
            }
            abrn = uv.getAbortingReason();
            if (abrn != null) abrn.execute();
            final List responses = getResponses(exec, uv, errs);
            if (rque.isEmpty()) doneReqIds = rque.clearPerfRequestIds(); else responses.add(new AuEcho(desktop));
            final int resId = desktopCtrl.getResponseId(true);
            desktopCtrl.responseSent(out.getChannel(), sid, new Object[] { new Integer(resId), responses });
            out.writeResponseId(resId);
            out.write(responses);
            cleaned = true;
            desktopCtrl.invokeExecutionCleanups(exec, null, errs);
            config.invokeExecutionCleanups(exec, null, errs);
        } catch (Throwable ex) {
            if (!cleaned) {
                cleaned = true;
                final List errs = new LinkedList();
                errs.add(ex);
                desktopCtrl.invokeExecutionCleanups(exec, null, errs);
                config.invokeExecutionCleanups(exec, null, errs);
                ex = errs.isEmpty() ? null : (Throwable) errs.get(0);
            }
            if (ex != null) {
                if (ex instanceof IOException) throw (IOException) ex;
                throw UiException.Aide.wrap(ex);
            }
        } finally {
            if (!cleaned) {
                desktopCtrl.invokeExecutionCleanups(exec, null, null);
                config.invokeExecutionCleanups(exec, null, null);
            }
            if (abrn != null) {
                try {
                    abrn.finish();
                } catch (Throwable t) {
                    log.warning(t);
                }
            }
            if (monitor != null) {
                try {
                    monitor.afterUpdate(desktop);
                } catch (Throwable ex) {
                    log.error(ex);
                }
            }
            doDeactivate(exec);
            if (pfmeter != null && doneReqIds != null) meterAuServerComplete(pfmeter, doneReqIds, exec);
        }
    }
