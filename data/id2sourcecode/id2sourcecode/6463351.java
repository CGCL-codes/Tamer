    public final void sample(float position, Keyframe output) {
        assert output != null;
        assert output.translation != null;
        assert output.rotation != null;
        assert output.scale != null;
        if (timestamps == null || keyframes == null) return;
        int min = 0;
        int max = timestamps.length;
        while (min < max - 1) {
            int pos = (min + max) / 2;
            if (timestamps[pos] > position) max = pos; else min = pos;
        }
        float maxValue;
        if (min == timestamps.length - 1) {
            max = 0;
            maxValue = duration;
        } else maxValue = timestamps[max];
        float minValue = timestamps[min];
        float interpolate = (position - minValue) / (maxValue - minValue);
        Keyframe a = keyframes[min];
        Keyframe b = keyframes[max];
        lerp(a.translation, b.translation, interpolate, output.translation);
        lerp(a.scale, b.scale, interpolate, output.scale);
        lerp(a.rotation, b.rotation, interpolate, output.rotation);
    }
