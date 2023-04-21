    public void resetLoc() {
        _locX = (_locX2 + _locX1) / 2;
        _locY = (_locY2 + _locY1) / 2;
        _location = new L1Location(_locX, _locY, _mapId);
    }
