    private void methodEntryEvent(MethodEntryEvent event) {
        Debug.out.println(DREV, "methodEntryEvent");
        if (event.method().toString().endsWith(".main(java.lang.String[])")) {
            MethodExitRequest mexr = _eventRequestManager.createMethodExitRequest();
            _mainClassName = event.method().declaringType().name();
            mexr.addClassFilter(_mainClassName);
            mexr.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            mexr.enable();
            _mainMethodEntryRequest.disable();
            _writer.println("// Note: " + _mainClassName + ".main entered");
            _ra.enableReplay();
        } else if (event.method().toString().startsWith("edu.rice.cs.cunit.SyncPointBuffer.replayTransfer")) {
            _writer.println("// Note: Updating sync points. edu.rice.cs.cunit.SyncPointBuffer.replayTransfer entered by thread id " + event.thread().uniqueID());
            _ra.loadSynchronizationPoints();
        }
    }
