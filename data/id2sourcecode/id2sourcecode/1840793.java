    public void updateEPG(int channel, List<EventDescription> update) {
        Station station = this.getStation(ServiceType.TV, channel);
        Resource ch = station.getChannel();
        Collections.sort(update, new EventDescription.DateComparator());
        while (this.epg.size() <= channel) this.epg.add(null);
        Channel cepg = this.epg.get(channel);
        if (cepg == null) {
            cepg = new Channel(this, ch, channel);
            this.epg.set(channel, cepg);
        }
        cepg.updateEvents(update);
        this.firePropertyChange("epg", null, this.epg);
    }
