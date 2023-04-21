    public static void main(String args[]) {
        try {
            for (MidiDevice.Info inf : MidiSystem.getMidiDeviceInfo()) {
                if (inf.getName().equals("Gervill")) {
                    Synthesizer dev = (Synthesizer) MidiSystem.getMidiDevice(inf);
                    dev.open();
                    String sf = "/home/pjl/frinika/soundfonts/ChoriumRevA.SF2";
                    Soundbank sbk;
                    sbk = MidiSystem.getSoundbank(new File(sf));
                    dev.loadAllInstruments(sbk);
                    Instrument insts[] = dev.getLoadedInstruments();
                    for (Instrument ins : insts) {
                        System.out.print("\n ***************** INST :" + ins);
                        System.out.println(ins.getName() + " " + ins.getPatch().getBank() + " " + ins.getPatch().getProgram() + " ");
                        Method getChannels = ins.getClass().getMethod("getChannels");
                        boolean[] channels = null;
                        if (getChannels != null) {
                            channels = (boolean[]) getChannels.invoke(ins, (Object[]) null);
                            if (channels[9]) {
                                Method getKeys = ins.getClass().getMethod("getKeys");
                                if (getKeys != null) {
                                    String[] keyNames = (String[]) getKeys.invoke(ins, (Object[]) null);
                                    int i = 0;
                                    for (String keyname : keyNames) {
                                        if (keyname != null) System.out.println((i++) + ":" + keyname);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(" HIT cntrl-C ");
            Thread.sleep(100000);
        } catch (Exception ex) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(ListKeyNames.class.getName()).log(Level.SEVERE, null, ex1);
            }
            ex.printStackTrace();
        }
    }
