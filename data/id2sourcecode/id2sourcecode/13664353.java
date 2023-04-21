    private synchronized void saveHOMStates() {
        ch1 = new Channel[rfCavs.size()];
        ch2 = new Channel[rfCavs.size()];
        hom0States = new int[rfCavs.size()];
        hom1States = new int[rfCavs.size()];
        ChannelFactory cf = ChannelFactory.defaultFactory();
        Iterator it = rfCavs.iterator();
        int i = 0;
        while (it.hasNext()) {
            String cavName = ((AcceleratorNode) it.next()).getId();
            String chName0 = cavName.replaceAll("RF:Cav", "LLRF:HPM").concat(":HBADC0_Ctl");
            String chName1 = cavName.replaceAll("RF:Cav", "LLRF:HPM").concat(":HBADC1_Ctl");
            ch1[i] = cf.getChannel(chName0);
            ch2[i] = cf.getChannel(chName1);
            try {
                hom0States[i] = Integer.parseInt(ch1[i].getValueRecord().stringValue());
                hom1States[i] = Integer.parseInt(ch2[i].getValueRecord().stringValue());
            } catch (ConnectionException ce) {
                System.out.println("Cannot connect to a channel!");
            } catch (GetException ge) {
                System.out.println("Cannot get a channel value!");
            }
            i++;
        }
        finished1 = true;
        this.notify();
    }
