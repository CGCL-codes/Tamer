    public void add(float f) {
        for (int i = 0; i < (_x - 1); i++) _data[i] = _data[i + 1];
        _data[(_x - 1)] = f;
        if (f > _max) _max = f;
        repaint();
    }
