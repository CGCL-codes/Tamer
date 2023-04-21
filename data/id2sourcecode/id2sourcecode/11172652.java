    public String toHSLString() {
        float h, s, l;
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));
        float delta = max - min;
        l = (max + min) / 2;
        if (delta == 0) {
            h = 0;
            s = 0;
        } else {
            if (l < 0.5f) s = delta / (max + min); else s = delta / (2 - max - min);
            float deltaRed = (((max - r) / 6f) + (delta / 2f)) / delta;
            float deltaGreen = (((max - g) / 6f) + (delta / 2f)) / delta;
            float deltaBlue = (((max - b) / 6f) + (delta / 2f)) / delta;
            if (r == max) h = deltaBlue - deltaGreen; else if (g == max) h = (1f / 3f) + deltaRed - deltaBlue; else if (b == max) h = (2f / 3f) + deltaGreen - deltaRed; else throw new RuntimeException("hum hum, no color are the max. Impossible!");
            if (h < 0f) h += 1f;
            if (h > 1f) h -= 1f;
        }
        return String.format("hsl(%.2f, %.2f, %.2f)", h, s, l);
    }
