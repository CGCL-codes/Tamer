    private Color signal2color(double signal, Color low, Color high) {
        signal = signal > 1 ? 1 : signal;
        signal = signal < -1 ? -1 : signal;
        signal = (signal + 1) / 2;
        int index = (int) (signal * 255);
        Color result = signalCache[index];
        if (result == null) {
            float[] hsbLow = resolve(low);
            float[] hsbHigh = resolve(high);
            float h = transform(signal, hsbLow[0], hsbHigh[0]);
            float s = transform(signal, hsbLow[1], hsbHigh[1]);
            float b = transform(signal, hsbLow[2], hsbHigh[2]);
            result = new Color(Color.HSBtoRGB(h, s, b));
            signalCache[index] = result;
        }
        return result;
    }
