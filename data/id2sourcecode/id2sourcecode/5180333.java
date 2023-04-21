    private void methodEntryEvent(MethodEntryEvent event) {
        Debug.out.println(DREV, "methodEntryEvent");
        if (event.method().toString().endsWith(".main(java.lang.String[])")) {
            _mainMethodEntryRequest.disable();
            _viewAdapter.updateSynchronizationPoints(false, "main entered");
            _viewAdapter.setMainEntered();
        } else if ((event.method().toString().startsWith("edu.rice.cs.cunit.SyncPointBuffer.transfer")) || (event.method().toString().startsWith("edu.rice.cs.cunit.SyncPointBuffer.compactTransfer"))) {
            _writer.println("// Note: Updating sync points. edu.rice.cs.cunit.SyncPointBuffer.transfer or compactTransfer entered by thread id " + event.thread().uniqueID());
            _viewAdapter.updateSynchronizationPoints(true, "push");
        } else if (event.method().toString().startsWith("edu.rice.cs.cunit.SyncPointBuffer.compactImmediateTransfer")) {
            _writer.println("// Note: Updating sync points. edu.rice.cs.cunit.SyncPointBuffer.compactImmediateTransfer entered by thread id " + event.thread().uniqueID());
            _viewAdapter.updateSynchronizationPointsImmediately(true, "immediate push");
        }
    }
