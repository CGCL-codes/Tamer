    private Coords tryToBuildBridge(Coords start, int direction) {
        if (!board.contains(start)) return null;
        Vector<Coords> hexes = new Vector<Coords>(7);
        Coords end = null;
        Coords next = start.translated(direction);
        while (hexes.size() < 6) {
            if (!board.contains(next)) {
                break;
            }
            if (!hexNeedsBridge(board.getHex(next))) {
                end = next;
                break;
            }
            hexes.add(next);
            next = next.translated(direction);
        }
        if (end != null) {
            if (hexes.size() == 0) return null;
            int elev1 = board.getHex(start).getElevation();
            int elev2 = board.getHex(end).getElevation();
            int elevBridge = board.getHex(end).terrainLevel(Terrains.BRIDGE);
            if (elevBridge >= 0) {
                if (Math.abs(elev2 + elevBridge - elev1) > 2) return null;
                elev1 = elev2 + elevBridge;
            } else {
                if (Math.abs(elev1 - elev2) > 4) {
                    return null;
                }
                elev1 = (elev1 + elev2) / 2;
            }
            int exits = (1 << direction) | (1 << ((direction + 3) % 6));
            int cf = mapSettings.getCityMinCF() + Compute.randomInt(1 + mapSettings.getCityMaxCF() - mapSettings.getCityMinCF());
            for (Enumeration<Coords> e = hexes.elements(); e.hasMoreElements(); ) {
                Coords c = e.nextElement();
                addBridge(board.getHex(c), exits, elev1, cf);
            }
            connectHexes(start, hexes.firstElement(), 1);
            connectHexes(end, hexes.lastElement(), 1);
        }
        return end;
    }
