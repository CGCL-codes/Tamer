        private Physical(List readers, List writers, String logicalId) {
            super(0);
            final boolean isLittleEndian = EngineThread.getGenericJob().getUnscopedBooleanOptionValue(OptionRegistry.LITTLE_ENDIAN);
            final boolean doSimpleMerge = EngineThread.getGenericJob().getUnscopedBooleanOptionValue(OptionRegistry.SIMPLE_STATE_ARBITRATION);
            setIDLogical(logicalId);
            getResetPort().setUsed(true);
            setConsumesReset(true);
            getClockPort().setUsed(true);
            setConsumesClock(true);
            for (Iterator iter = writers.iterator(); iter.hasNext(); ) {
                if (iter.next() != null) {
                    Port enablePort = makeDataPort();
                    enablePort.setUsed(true);
                    enablePort.getPeer().setSize(1, false);
                    Port dataPort = makeDataPort();
                    dataPort.setUsed(true);
                    dataPort.getPeer().setSize(Register.this.getInitWidth(), Register.this.isSigned());
                }
            }
            BigInteger initValue = AddressableUnit.getCompositeValue(getInitialValue().getRep(), getInitialValue().getAddressStridePolicy());
            Constant initConstant = new SimpleConstant(initValue, Register.this.getInitWidth(), Register.this.isSigned());
            Bus dataSource = null;
            EndianSwapper inSwapper = null;
            EndianSwapper outSwapper = null;
            if (writers.size() > 0) {
                Reg reg = Reg.getConfigurableReg(Reg.REGRE, logicalId);
                reg.getResultBus().setSize(getInitWidth(), false);
                Value init = initConstant.getValueBus().getValue();
                reg.setInitialValue(init);
                reg.getClockPort().setBus(getClockPort().getPeer());
                reg.getResetPort().setBus(getResetPort().getPeer());
                reg.getInternalResetPort().setBus(getResetPort().getPeer());
                if (writers.size() == 1) {
                    assert this.getDataPorts().size() == 2;
                    reg.getEnablePort().setBus(((Port) getDataPorts().get(0)).getPeer());
                    reg.getDataPort().setBus(((Port) getDataPorts().get(1)).getPeer());
                } else {
                    if (doSimpleMerge) {
                        Mux mux = new Mux(writers.size());
                        Or or = new Or(writers.size());
                        Iterator dataMuxPorts = mux.getGoPorts().iterator();
                        Iterator writeOrPorts = or.getDataPorts().iterator();
                        Iterator physicalPorts = getDataPorts().iterator();
                        for (int i = 0; i < writers.size(); i++) {
                            Bus enable = ((Port) physicalPorts.next()).getPeer();
                            Bus data = ((Port) physicalPorts.next()).getPeer();
                            Port dataMuxGoPort = (Port) dataMuxPorts.next();
                            dataMuxGoPort.setBus(enable);
                            Port dataMuxDataPort = mux.getDataPort(dataMuxGoPort);
                            dataMuxDataPort.setBus(data);
                            ((Port) writeOrPorts.next()).setBus(enable);
                        }
                        addComponent(mux);
                        addComponent(or);
                        reg.getDataPort().setBus(mux.getResultBus());
                        reg.getEnablePort().setBus(or.getResultBus());
                    } else {
                        assert false : "Not supporting multiple writers in arbitrated register case.  See comment";
                        RegisterReferee referee = new RegisterReferee(Register.this, readers, writers);
                        addComponent(referee);
                        referee.connectImplementation(reg, this.getDataPorts());
                        addFeedbackPoint(reg);
                        addFeedbackPoint(referee);
                    }
                }
                if (!isLittleEndian && (getInitWidth() > 8)) {
                    inSwapper = new EndianSwapper(getInitWidth(), getInitialValue().getAddressStridePolicy().getStride());
                    inSwapper.getInputPort().setBus(reg.getDataPort().getBus());
                    reg.getDataPort().setBus(inSwapper.getOutputBus());
                    addComponent(inSwapper);
                }
                addComponent(reg);
                dataSource = reg.getResultBus();
            } else {
                addComponent(initConstant);
                dataSource = initConstant.getValueBus();
            }
            if (!isLittleEndian && (getInitWidth() > 8)) {
                outSwapper = new EndianSwapper(getInitWidth(), getInitialValue().getAddressStridePolicy().getStride());
                outSwapper.getInputPort().setBus(dataSource);
                dataSource = outSwapper.getOutputBus();
                addComponent(outSwapper);
            }
            this.registerOutput = makeExit(0).makeDataBus();
            this.registerOutput.setSize(getInitWidth(), isSigned());
            this.registerOutput.getPeer().setBus(dataSource);
        }
