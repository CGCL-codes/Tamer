    public static int combinePixels(int rgb1, int rgb2, int op, int extraAlpha) {
        if (op == REPLACE) return rgb1;
        int a1 = (rgb1 >> 24) & 0xff;
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = rgb1 & 0xff;
        int a2 = (rgb2 >> 24) & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = rgb2 & 0xff;
        switch(op) {
            case NORMAL:
                break;
            case MIN:
                r1 = Math.min(r1, r2);
                g1 = Math.min(g1, g2);
                b1 = Math.min(b1, b2);
                break;
            case MAX:
                r1 = Math.max(r1, r2);
                g1 = Math.max(g1, g2);
                b1 = Math.max(b1, b2);
                break;
            case ADD:
                r1 = clamp(r1 + r2);
                g1 = clamp(g1 + g2);
                b1 = clamp(b1 + b2);
                break;
            case SUBTRACT:
                r1 = clamp(r2 - r1);
                g1 = clamp(g2 - g1);
                b1 = clamp(b2 - b1);
                break;
            case DIFFERENCE:
                r1 = clamp(Math.abs(r1 - r2));
                g1 = clamp(Math.abs(g1 - g2));
                b1 = clamp(Math.abs(b1 - b2));
                break;
            case MULTIPLY:
                r1 = clamp(r1 * r2 / 255);
                g1 = clamp(g1 * g2 / 255);
                b1 = clamp(b1 * b2 / 255);
                break;
            case DISSOLVE:
                if ((randomGenerator.nextInt() & 0xff) <= a1) {
                    r1 = r2;
                    g1 = g2;
                    b1 = b2;
                }
                break;
            case AVERAGE:
                r1 = (r1 + r2) / 2;
                g1 = (g1 + g2) / 2;
                b1 = (b1 + b2) / 2;
                break;
            case HUE:
            case SATURATION:
            case VALUE:
            case COLOR:
                Color.RGBtoHSB(r1, g1, b1, hsb1);
                Color.RGBtoHSB(r2, g2, b2, hsb2);
                switch(op) {
                    case HUE:
                        hsb2[0] = hsb1[0];
                        break;
                    case SATURATION:
                        hsb2[1] = hsb1[1];
                        break;
                    case VALUE:
                        hsb2[2] = hsb1[2];
                        break;
                    case COLOR:
                        hsb2[0] = hsb1[0];
                        hsb2[1] = hsb1[1];
                        break;
                }
                rgb1 = Color.HSBtoRGB(hsb2[0], hsb2[1], hsb2[2]);
                r1 = (rgb1 >> 16) & 0xff;
                g1 = (rgb1 >> 8) & 0xff;
                b1 = rgb1 & 0xff;
                break;
            case SCREEN:
                r1 = 255 - ((255 - r1) * (255 - r2)) / 255;
                g1 = 255 - ((255 - g1) * (255 - g2)) / 255;
                b1 = 255 - ((255 - b1) * (255 - b2)) / 255;
                break;
            case OVERLAY:
                int m, s;
                s = 255 - ((255 - r1) * (255 - r2)) / 255;
                m = r1 * r2 / 255;
                r1 = (s * r1 + m * (255 - r1)) / 255;
                s = 255 - ((255 - g1) * (255 - g2)) / 255;
                m = g1 * g2 / 255;
                g1 = (s * g1 + m * (255 - g1)) / 255;
                s = 255 - ((255 - b1) * (255 - b2)) / 255;
                m = b1 * b2 / 255;
                b1 = (s * b1 + m * (255 - b1)) / 255;
                break;
            case CLEAR:
                r1 = g1 = b1 = 0xff;
                break;
            case DST_IN:
                r1 = clamp((r2 * a1) / 255);
                g1 = clamp((g2 * a1) / 255);
                b1 = clamp((b2 * a1) / 255);
                a1 = clamp((a2 * a1) / 255);
                return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
            case ALPHA:
                a1 = a1 * a2 / 255;
                return (a1 << 24) | (r2 << 16) | (g2 << 8) | b2;
            case ALPHA_TO_GRAY:
                int na = 255 - a1;
                return (a1 << 24) | (na << 16) | (na << 8) | na;
        }
        if (extraAlpha != 0xff || a1 != 0xff) {
            a1 = a1 * extraAlpha / 255;
            int a3 = (255 - a1) * a2 / 255;
            r1 = clamp((r1 * a1 + r2 * a3) / 255);
            g1 = clamp((g1 * a1 + g2 * a3) / 255);
            b1 = clamp((b1 * a1 + b2 * a3) / 255);
            a1 = clamp(a1 + a3);
        }
        return (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
    }
