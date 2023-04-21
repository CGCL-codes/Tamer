    public float getControlValue(HeliControl control) {
        if (axisMap.containsKey(control)) {
            int axis = axisMap.get(control);
            if (axis >= 0) {
                return getChannelValue(axis, false);
            }
        }
        return 0.5f;
    }
