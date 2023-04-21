    private void exitModule(Module module) {
        Frame frame = currentFrame;
        currentFrame = (Frame) accessFrames.pop();
        for (Iterator iter = frame.getResources().iterator(); iter.hasNext(); ) {
            final LogicalMemoryPort memoryPort = (LogicalMemoryPort) iter.next();
            ResourceBundle bundle = frame.getBundle(memoryPort);
            List reads = bundle.getReads();
            List writes = bundle.getWrites();
            MemoryGateway gateway = new MemoryGateway(memoryPort, reads.size(), writes.size(), memoryPort.getMaxAddressWidth());
            module.addComponent(gateway);
            for (int i = 0; i < reads.size(); i++) {
                ((MemAccess) reads.get(i)).connect(gateway, i);
            }
            for (int i = 0; i < writes.size(); i++) {
                ((MemAccess) writes.get(i)).connect(gateway, i);
            }
            Map duplicate = new HashMap();
            if (reads.size() > 0) {
                currentFrame.addAccess(new MemReadAccess(gateway, module, duplicate));
            }
            if (writes.size() > 0) {
                currentFrame.addAccess(new MemWriteAccess(gateway, module, duplicate));
            }
        }
    }
