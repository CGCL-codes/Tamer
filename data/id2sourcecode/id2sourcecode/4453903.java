    public void send(MidiMessage mess, long arg1) {
        ShortMessage smsg = (ShortMessage) mess;
        System.out.println("ch cmd data1 data2: " + smsg.getChannel() + " " + smsg.getCommand() + " " + smsg.getData1() + " " + smsg.getData2());
        double t = valueizer.getValue((ShortMessage) mess);
        System.out.println(" Send message to " + cntrl + " " + t);
        if (cntrl instanceof ControlLaw) {
            ControlLaw law = ((LawControl) cntrl).getLaw();
            float val = (float) (law.getMaximum() * t + law.getMinimum() * (1 - t));
            ((LawControl) cntrl).setValue(val);
        } else if (cntrl instanceof BooleanControl) {
            System.out.println(" BOOLEAN");
            ((BooleanControl) cntrl).setValue(t > 0);
        } else if (cntrl instanceof FloatControl) {
            ControlLaw law = ((FloatControl) cntrl).getLaw();
            float val = (float) (law.getMaximum() * t + law.getMinimum() * (1 - t));
            ((LawControl) cntrl).setValue(val);
        } else {
            try {
                throw new Exception(" Unknown control ");
            } catch (Exception ex) {
                Logger.getLogger(ControlMapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
