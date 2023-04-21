    private void methodExitEvent(MethodExitEvent event) {
        Debug.out.println(DREV, "methodExitEvent");
        if (event.method().toString().startsWith("java.lang.Thread.exit")) {
            _viewAdapter.updateSynchronizationPoints(false, "User thread exited");
        } else if ((event.method().toString().startsWith("edu.rice.cs.cunit.SyncPointBuffer.add")) || (event.method().toString().startsWith("edu.rice.cs.cunit.SyncPointBuffer.compactAdd"))) {
            _writer.println("// Note: Updating sync points. edu.rice.cs.cunit.SyncPointBuffer.add or compactAdd exited by thread id " + event.thread().uniqueID());
            String lastReason = _pullReason;
            setSyncPointBufferExitRequestEnable(false, "");
            _viewAdapter.updateSynchronizationPoints(false, "delayed pull (" + lastReason + ")");
        }
    }
