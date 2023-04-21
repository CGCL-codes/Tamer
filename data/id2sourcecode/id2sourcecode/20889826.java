        TaskCapture(int stateWidth, int depth, int width, Bus resetBus, Bus doneBus, Bus dataOutBus, TaskSlot ts, int index, boolean combinationalRead) {
            boolean readUsed = ts.getGoRPort() != null;
            boolean writeUsed = ts.getGoWPort() != null;
            Bus goRBus = readUsed ? ts.getGoRPort().getPeer() : null;
            Bus goWBus = writeUsed ? ts.getGoWPort().getPeer() : null;
            Bus addrBus = ts.getAddressPort().getPeer();
            Bus sizeBus = ts.getSizePort().getPeer();
            Bus dataInBus = writeUsed ? ts.getDataInPort().getPeer() : null;
            Bus clockBus = getClockPort().getPeer();
            Bus oneBus = getOneConstant().getValueBus();
            Reg readGoReg = null;
            And readAnd = null;
            Not readNot = null;
            EncodedMux readMux = null;
            if (readUsed) {
                readGoReg = Reg.getConfigurableReg(Reg.REGR, ID.showLogical(this) + "TCreadGoReg_" + index);
                addComponent(readGoReg);
                readGoReg.getInternalResetPort().setBus(resetBus);
                readGoReg.getClockPort().setBus(clockBus);
                feedbackPoints.add(readGoReg);
                readGoReg.getResultBus().setSize(1, false);
                readAnd = new And(2);
                addComponent(readAnd);
                readAnd.setIDLogical(this, "TCreadAnd_" + index);
                readNot = new Not();
                addComponent(readNot);
                readNot.setIDLogical(this, "TCreadNot_" + index);
                readMux = new EncodedMux(2);
                addComponent(readMux);
                readMux.setIDLogical(this, "TCreadMux_" + index);
            }
            if (!readUsed || !writeUsed) {
                readOr = new Or(2);
            } else {
                readOr = new Or(4);
            }
            addComponent(readOr);
            readOr.setIDLogical(this, "TCreadOr_" + index);
            Reg writeGoReg = null;
            And writeAnd = null;
            Not writeNot = null;
            EncodedMux writeMux = null;
            if (writeUsed) {
                writeGoReg = Reg.getConfigurableReg(Reg.REGR, ID.showLogical(this) + "TCwriteGoReg_" + index);
                addComponent(writeGoReg);
                writeGoReg.getInternalResetPort().setBus(resetBus);
                writeGoReg.getClockPort().setBus(clockBus);
                feedbackPoints.add(writeGoReg);
                writeGoReg.getResultBus().setSize(1, false);
                writeAnd = new And(2);
                addComponent(writeAnd);
                writeAnd.setIDLogical(this, "TCwriteAnd_" + index);
                writeNot = new Not();
                addComponent(writeNot);
                writeNot.setIDLogical(this, "TCwriteNot_" + index);
                writeMux = new EncodedMux(2);
                addComponent(writeMux);
                writeMux.setIDLogical(this, "TCwriteMux_" + index);
                writeOr = new Or(2);
                addComponent(writeOr);
                writeOr.setIDLogical(this, "TCwriteOr_" + index);
            }
            Or addrOr = null;
            if (readUsed && writeUsed) {
                addrOr = new Or(2);
                addComponent(addrOr);
                addrOr.setIDLogical(this, "TCaddrOr_" + index);
            }
            Reg addrReg = Reg.getConfigurableReg(Reg.REGR, ID.showLogical(this) + "TCaddrReg_" + index);
            addComponent(addrReg);
            addrReg.getInternalResetPort().setBus(resetBus);
            addrReg.getClockPort().setBus(clockBus);
            feedbackPoints.add(addrReg);
            addrReg.getResultBus().setSize(depth, false);
            addrRegMux = new EncodedMux(2);
            addComponent(addrRegMux);
            addrRegMux.setIDLogical(this, "TCaddrRegMux_" + index);
            Reg sizeReg = Reg.getConfigurableReg(Reg.REGR, ID.showLogical(this) + "TCsizeReg_" + index);
            addComponent(sizeReg);
            sizeReg.getInternalResetPort().setBus(resetBus);
            sizeReg.getClockPort().setBus(clockBus);
            feedbackPoints.add(sizeReg);
            sizeReg.getResultBus().setSize(LogicalMemory.SIZE_WIDTH, false);
            sizeRegMux = new EncodedMux(2);
            addComponent(sizeRegMux);
            sizeRegMux.setIDLogical(this, "TCsizeRegMux_" + index);
            Reg dinReg = null;
            dinMux = null;
            if (writeUsed) {
                dinReg = Reg.getConfigurableReg(Reg.REGR, ID.showLogical(this) + "TCdinReg_" + index);
                addComponent(dinReg);
                dinReg.getInternalResetPort().setBus(resetBus);
                dinReg.getClockPort().setBus(clockBus);
                dinReg.getResultBus().setSize(width, false);
                feedbackPoints.add(dinReg);
                dinMux = new EncodedMux(2);
                addComponent(dinMux);
                dinMux.setIDLogical(this, "TCdinMux_" + index);
            }
            ts.getDoneBus().getPeer().setBus(doneBus);
            if (readUsed) {
                readMux.getSelectPort().setBus(goRBus);
                readMux.getDataPort(1).setBus(oneBus);
                if (combinationalRead) {
                    readMux.getDataPort(0).setBus(readGoReg.getResultBus());
                    readGoReg.getDataPort().setBus(readAnd.getResultBus());
                    ((Port) readAnd.getDataPorts().get(0)).setBus(readMux.getResultBus());
                } else {
                    readMux.getDataPort(0).setBus(readAnd.getResultBus());
                    readGoReg.getDataPort().setBus(readMux.getResultBus());
                    ((Port) readAnd.getDataPorts().get(0)).setBus(readGoReg.getResultBus());
                }
                ((Port) readAnd.getDataPorts().get(1)).setBus(readNot.getResultBus());
                readNot.getDataPort().setBus(doneBus);
            }
            if (writeUsed) {
                writeMux.getSelectPort().setBus(goWBus);
                writeMux.getDataPort(1).setBus(oneBus);
                writeMux.getDataPort(0).setBus(writeAnd.getResultBus());
                writeGoReg.getDataPort().setBus(writeMux.getResultBus());
                ((Port) writeAnd.getDataPorts().get(0)).setBus(writeGoReg.getResultBus());
                ((Port) writeAnd.getDataPorts().get(1)).setBus(writeNot.getResultBus());
                writeNot.getDataPort().setBus(doneBus);
            }
            if (readUsed && writeUsed) {
                if (combinationalRead) {
                    ((Port) readOr.getDataPorts().get(0)).setBus(readGoReg.getResultBus());
                } else {
                    ((Port) readOr.getDataPorts().get(0)).setBus(readAnd.getResultBus());
                }
                ((Port) readOr.getDataPorts().get(1)).setBus(goRBus);
                ((Port) readOr.getDataPorts().get(2)).setBus(writeAnd.getResultBus());
                ((Port) readOr.getDataPorts().get(3)).setBus(goWBus);
                ((Port) writeOr.getDataPorts().get(0)).setBus(writeAnd.getResultBus());
                ((Port) writeOr.getDataPorts().get(1)).setBus(goWBus);
            } else if (readUsed) {
                if (combinationalRead) {
                    ((Port) readOr.getDataPorts().get(0)).setBus(readGoReg.getResultBus());
                } else {
                    ((Port) readOr.getDataPorts().get(0)).setBus(readAnd.getResultBus());
                }
                ((Port) readOr.getDataPorts().get(1)).setBus(goRBus);
            } else {
                ((Port) readOr.getDataPorts().get(0)).setBus(writeAnd.getResultBus());
                ((Port) readOr.getDataPorts().get(1)).setBus(goWBus);
                ((Port) writeOr.getDataPorts().get(0)).setBus(writeAnd.getResultBus());
                ((Port) writeOr.getDataPorts().get(1)).setBus(goWBus);
            }
            Bus addrSelectBus;
            if (readUsed && writeUsed) {
                ((Port) addrOr.getDataPorts().get(0)).setBus(goRBus);
                ((Port) addrOr.getDataPorts().get(1)).setBus(goWBus);
                addrSelectBus = addrOr.getResultBus();
            } else if (readUsed) {
                addrSelectBus = goRBus;
            } else {
                addrSelectBus = goWBus;
            }
            addrRegMux.getSelectPort().setBus(addrSelectBus);
            addrRegMux.getDataPort(0).setBus(addrReg.getResultBus());
            addrRegMux.getDataPort(1).setBus(addrBus);
            addrReg.getDataPort().setBus(addrRegMux.getResultBus());
            sizeRegMux.getSelectPort().setBus(addrSelectBus);
            sizeRegMux.getDataPort(0).setBus(sizeReg.getResultBus());
            sizeRegMux.getDataPort(1).setBus(sizeBus);
            sizeReg.getDataPort().setBus(sizeRegMux.getResultBus());
            if (writeUsed) {
                dinMux.getSelectPort().setBus(goWBus);
                dinMux.getDataPort(0).setBus(dinReg.getResultBus());
                dinMux.getDataPort(1).setBus(dataInBus);
                dinReg.getDataPort().setBus(dinMux.getResultBus());
            }
            if (readUsed) {
                ts.getDataOutBus().getPeer().setBus(dataOutBus);
            }
        }
