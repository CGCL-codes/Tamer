    public Color getBoardColor() {
        if (_boardColor == null) _boardColor = new Color(0x66, 0x99, 0x33);
        if (_boardColor == null) {
            Iterator iter = getPlayers();
            float[] floats = new float[3];
            int hue = 0, lum = 0, sat = 0;
            while (iter.hasNext()) {
                Color pColor = ((IPlayerInfo) iter.next()).getColor();
                Color.RGBtoHSB(pColor.getRed(), pColor.getGreen(), pColor.getBlue(), floats);
                int h = (int) (floats[0] * 255.0);
                int l = (int) (floats[1] * 255.0);
                int s = (int) (floats[2] * 255.0);
                hue ^= h;
                lum = (lum + l) / 2;
                sat = (sat + s) / 2;
            }
            _boardColor = new Color(Color.HSBtoRGB(((float) hue) / 255.0f, ((float) lum) / 255.0f, ((float) sat) / 255.0f));
        }
        return _boardColor;
    }
