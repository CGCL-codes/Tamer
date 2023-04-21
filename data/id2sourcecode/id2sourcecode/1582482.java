        public void propagateSignals() {
            Iterator i = pinWires.iterator();
            PinWire currOutput = null;
            while (i.hasNext()) {
                PinWire curr = (PinWire) i.next();
                if (curr.outputReady()) {
                    if (currOutput != null) {
                        String s = "ERROR: More than one output wire on this PinLink";
                        System.out.println(s);
                        return;
                    } else {
                        currOutput = curr;
                    }
                }
            }
            if (currOutput == null) {
            } else {
                i = pinWires.iterator();
                while (i.hasNext()) {
                    PinWire curr = (PinWire) i.next();
                    if (curr != currOutput) {
                        curr.wireOutput.write(currOutput.wireInput.read());
                    }
                }
            }
        }
