    private void stateDones(MemoryPortDef mp, Module memoryModule, boolean regRead, String X) {
        SequentialBlock delayBlock = new SequentialBlock();
        Register weDone = new Register("we" + X + "_done", 1);
        if (mp.writes()) {
            delayBlock.add(new Assign.NonBlocking(weDone, mp.wen));
        }
        Register reDone = new Register("re" + X + "_done", 1);
        if (mp.reads() && regRead) {
            delayBlock.add(new Assign.NonBlocking(reDone, mp.ren));
        }
        EventControl clkEvent = new EventControl(new EventExpression.PosEdge(this.clkPort));
        memoryModule.state(new Always(new ProceduralTimingBlock(clkEvent, delayBlock)));
        if (mp.writes() && mp.reads()) {
            if (regRead) {
                memoryModule.state(new Assign.Continuous(mp.done, new Bitwise.Or(new Bitwise.And(mp.ren, new Unary.Not(mp.wen)), weDone)));
            } else {
                memoryModule.state(new Assign.Continuous(mp.done, new Bitwise.Or(reDone, weDone)));
            }
        } else if (mp.writes()) {
            memoryModule.state(new Assign.Continuous(mp.done, weDone));
        } else {
            Net ren_done = (regRead) ? (Net) reDone : (Net) mp.ren;
            memoryModule.state(new Assign.Continuous(mp.done, ren_done));
        }
    }
