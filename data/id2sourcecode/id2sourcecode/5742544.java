    @Override
    public void acceleratorChanged() {
        if (accelerator != null) {
            stopPCAS();
            StringBuffer description = new StringBuffer("Selected Accelerator: " + accelerator.getId() + '\n');
            description.append("Sequences:\n");
            Iterator sequenceIter = accelerator.getSequences().iterator();
            while (sequenceIter.hasNext()) {
                AcceleratorSeq sequence = (AcceleratorSeq) sequenceIter.next();
                description.append('\t' + sequence.getId() + '\n');
            }
            beamOnEvent = accelerator.getTimingCenter().getChannel(TimingCenter.BEAM_ON_EVENT_HANDLE);
            beamOnEventCount = accelerator.getTimingCenter().getChannel(TimingCenter.BEAM_ON_EVENT_COUNT_HANDLE);
            setHasChanges(true);
        }
    }
