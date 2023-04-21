    public void updateState(RendezVousHeartbeatEvent event, HashMap<OverlayNetwork, Collection<PeerRegister>> control) {
        this.timeLastContact = event.getChannel().getTimeProvider().currentTimeMillis();
        Collection<OverlayNetwork> temp = event.getOverlayNetworks();
        for (OverlayNetwork on : this.overlaysPresent) {
            if (!temp.contains((on))) {
                Collection<PeerRegister> list = control.get(on);
                list.remove(this);
                if (list.size() == 0) control.remove(on);
            }
        }
        for (OverlayNetwork on : temp) {
            if (!this.overlaysPresent.contains(on)) {
                Collection<PeerRegister> list = null;
                if (control.containsKey(on)) list = control.get(on); else {
                    list = new ArrayList<PeerRegister>();
                    control.put(on, list);
                }
                list.add(this);
                this.helpers.add(list);
                this.overlaysPresent.add(on);
            }
        }
    }
