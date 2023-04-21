    public MemoryReferee(Arbitratable resource, List readList, List writeList) {
        this(resource);
        assert readList.size() == writeList.size() : "readList must match writeList size";
        int numTaskSlots = readList.size();
        final int memoryWidth = resource.getDataPathWidth();
        final int addressWidth = resource.getAddrPathWidth();
        final boolean isAddressable = resource.isAddressable();
        final boolean combinationalMemoryReads = resource.allowsCombinationalReads();
        this.globalSlot.setSizes(memoryWidth, addressWidth, LogicalMemory.SIZE_WIDTH);
        getClockPort().setUsed(true);
        getResetPort().setUsed(true);
        zeroConstant = new SimpleConstant(0, 1);
        zeroConstant.setIDLogical(this, "zeroConstant");
        addComponent(zeroConstant);
        zeroDataConstant = new SimpleConstant(0, memoryWidth);
        zeroDataConstant.setIDLogical(this, "zeroDataConstant");
        addComponent(zeroDataConstant);
        oneConstant = new SimpleConstant(1, 1);
        oneConstant.setIDLogical(this, "oneConstant");
        addComponent(oneConstant);
        int stateWidth = 0;
        for (int i = numTaskSlots; i > 0; i = i >> 1) {
            stateWidth++;
        }
        for (int i = 0; i < numTaskSlots; i++) {
            boolean doesRead = readList.get(i) != null;
            boolean doesWrite = writeList.get(i) != null;
            TaskSlot ts = new TaskSlot(this, memoryWidth, addressWidth, doesRead, doesWrite);
            addTaskSlot(ts);
        }
        final int taskCount = getTaskSlots().size();
        if (taskCount == 1) {
            TaskSlot ts = (TaskSlot) getTaskSlots().get(0);
            boolean readUsed = ts.getGoRPort() != null;
            boolean writeUsed = ts.getGoWPort() != null;
            Or goOr = null;
            if (readUsed && writeUsed) {
                goOr = new Or(2);
                addComponent(goOr);
                goOr.setIDLogical(this, "goOr");
                ((Port) goOr.getDataPorts().get(0)).setBus(ts.getGoRPort().getPeer());
                ((Port) goOr.getDataPorts().get(1)).setBus(ts.getGoWPort().getPeer());
                globalSlot.getGoBus().getPeer().setBus(goOr.getResultBus());
                globalSlot.getWriteEnableBus().getPeer().setBus(ts.getGoWPort().getPeer());
            } else if (readUsed) {
                globalSlot.getGoBus().getPeer().setBus(ts.getGoRPort().getPeer());
                globalSlot.getWriteEnableBus().getPeer().setBus(getZeroConstant().getValueBus());
            } else {
                globalSlot.getGoBus().getPeer().setBus(ts.getGoWPort().getPeer());
                globalSlot.getWriteEnableBus().getPeer().setBus(ts.getGoWPort().getPeer());
            }
            globalSlot.getAddressBus().getPeer().setBus(ts.getAddressPort().getPeer());
            globalSlot.getSizeBus().getPeer().setBus(ts.getSizePort().getPeer());
            if (writeUsed) {
                globalSlot.getWriteDataBus().getPeer().setBus(ts.getDataInPort().getPeer());
            } else {
                globalSlot.getWriteDataBus().getPeer().setBus(zeroDataConstant.getValueBus());
            }
            if (readUsed) {
                ts.getDataOutBus().getPeer().setBus(globalSlot.getReadDataPort().getPeer());
            }
            ts.getDoneBus().getPeer().setBus(globalSlot.getDonePort().getPeer());
            return;
        }
        EncodedMux goMux = new EncodedMux(taskCount);
        addComponent(goMux);
        goMux.setIDLogical(this, "goMux");
        EncodedMux addrMux = null;
        if (isAddressable) {
            addrMux = new EncodedMux(taskCount);
            addComponent(addrMux);
            addrMux.setIDLogical(this, "addrMux");
        }
        EncodedMux sizeMux = new EncodedMux(taskCount);
        addComponent(sizeMux);
        sizeMux.setIDLogical(this, "sizeMux");
        EncodedMux dataInMux = new EncodedMux(taskCount);
        addComponent(dataInMux);
        dataInMux.setIDLogical(this, "dataInMux");
        EncodedMux writeEnableMux = new EncodedMux(taskCount);
        addComponent(writeEnableMux);
        writeEnableMux.setIDLogical(this, "writeEnableMux");
        Or advanceOr = new Or(taskCount);
        addComponent(advanceOr);
        advanceOr.setIDLogical(this, "advanceOr");
        StateMachine stateMachine = new StateMachine(getClockPort().getPeer(), getResetPort().getPeer(), advanceOr.getResultBus(), stateWidth);
        goMux.getSelectPort().setBus(stateMachine.getResultBus());
        if (addrMux != null) {
            addrMux.getSelectPort().setBus(stateMachine.getResultBus());
        }
        sizeMux.getSelectPort().setBus(stateMachine.getResultBus());
        dataInMux.getSelectPort().setBus(stateMachine.getResultBus());
        writeEnableMux.getSelectPort().setBus(stateMachine.getResultBus());
        List taskCaptures = new ArrayList();
        for (int i = 0; i < taskCount; i++) {
            DoneFilter df = new DoneFilter(globalSlot.getDonePort().getPeer(), stateMachine.getDelayStateBus(), i, stateWidth);
            TaskCapture tc = new TaskCapture(stateWidth, addressWidth, memoryWidth, getResetPort().getPeer(), df.getResultBus(), globalSlot.getReadDataPort().getPeer(), (TaskSlot) getTaskSlots().get(i), i, combinationalMemoryReads);
            taskCaptures.add(tc);
            this.feedbackPoints.addAll(tc.getFeedbackPoints());
            goMux.getDataPort(i).setBus(tc.getTaskMemGoBus());
            ((Port) advanceOr.getDataPorts().get(i)).setBus(tc.getTaskMemGoBus());
            if (addrMux != null) {
                addrMux.getDataPort(i).setBus(tc.getTaskMemAddrBus());
            }
            sizeMux.getDataPort(i).setBus(tc.getTaskMemSizeBus());
            final CastOp castOp = new CastOp(memoryWidth, false);
            addComponent(castOp);
            castOp.getDataPort().setBus(tc.getTaskMemDataInBus());
            dataInMux.getDataPort(i).setBus(castOp.getResultBus());
            writeEnableMux.getDataPort(i).setBus(tc.getTaskMemWrBus());
        }
        stateMachine.setTaskCaptures(taskCaptures);
        this.feedbackPoints.addAll(stateMachine.getFeedbackPoints());
        globalSlot.getGoBus().getPeer().setBus(goMux.getResultBus());
        if (addrMux != null) {
            globalSlot.getAddressBus().getPeer().setBus(addrMux.getResultBus());
        } else {
            Constant addrConst = new SimpleConstant(0, addressWidth);
            globalSlot.getAddressBus().getPeer().setBus(addrConst.getValueBus());
        }
        globalSlot.getSizeBus().getPeer().setBus(sizeMux.getResultBus());
        globalSlot.getWriteDataBus().getPeer().setBus(dataInMux.getResultBus());
        globalSlot.getWriteEnableBus().getPeer().setBus(writeEnableMux.getResultBus());
    }
