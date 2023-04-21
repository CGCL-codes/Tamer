    public PeerRegister(RendezVousHeartbeatEvent event, HashMap<OverlayNetwork, Collection<PeerRegister>> control) {
        this.peer = event.getPeerID();
        this.timeLastContact = event.getChannel().getTimeProvider().currentTimeMillis();
        this.helpers = new ArrayList<Collection<PeerRegister>>();
        this.overlaysPresent = new ArrayList<OverlayNetwork>();
        for (OverlayNetwork on : event.getOverlayNetworksArray()) {
            Collection<PeerRegister> help = null;
            if (control.containsKey(on)) help = control.get(on); else {
                help = new ArrayList<PeerRegister>();
                control.put(on, help);
            }
            this.helpers.add(help);
            help.add(this);
            this.overlaysPresent.add(on);
        }
    }
