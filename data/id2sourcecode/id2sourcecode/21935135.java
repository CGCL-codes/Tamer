    protected boolean addSwatchData(CellSwatchRecord swatch, long updateNumber) {
        if (this.updateNumber != updateNumber) beginSwatchUpdate(updateNumber);
        DataStore ds = lrd.datastore;
        final String inputChannel = swatch.getInputChannel();
        if (inputChannel.equals(CellSwatchRecord.ALARMSWATCH)) {
            Strip alarmStrip = ds.getAlarmStrip();
            if (alarmStrip == null) return false;
            swatch.setSigFunction(alarmStrip.getSigFunction());
            Cell cell = getCreateCell(alarmStrip);
            cell.setSwatch(swatch);
            cell.checkAddToCritGroup();
            return true;
        }
        List<Strip> stripSet = ds.getChannelStripList(inputChannel);
        final int numStrips = stripSet.size();
        Iterator<Strip> it = stripSet.iterator();
        while (it.hasNext()) {
            final Strip strip = it.next();
            swatch.setSigFunction(strip.getSigFunction());
            Cell cell = getCreateCell(strip);
            cell.setSwatch(swatch);
            cell.checkAddToCritGroup();
        }
        if (numStrips == 0) System.err.println("Source.addSwatchData: unable to locate strip for swatch (" + swatch + ")");
        return numStrips > 0;
    }
