    public String getChannelId(Overlay overlay) {
        if (!(overlay instanceof Polyline)) {
            return null;
        }
        if (lineToIdMap == null) {
            lineToIdMap = new HashMap<Polyline, String>();
            for (String id : lineMap.keySet()) {
                lineToIdMap.put(lineMap.get(id), id);
            }
        }
        Polyline line = (Polyline) overlay;
        return lineToIdMap.get(line);
    }
