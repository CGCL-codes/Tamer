    private void updateHSL() {
        awt = null;
        double var_R = ((double) ((color & 0xFF0000) >> 16) / 255);
        double var_G = ((double) ((color & 0x00FF00) >> 8) / 255);
        double var_B = ((double) ((color & 0x0000FF)) / 255);
        double var_Min = Math.min(var_R, Math.min(var_G, var_B));
        double var_Max = Math.max(var_R, Math.max(var_G, var_B));
        double del_Max = var_Max - var_Min;
        lightness = (var_Max + var_Min) / 2;
        hue = 0;
        saturation = 0;
        if (del_Max != 0) {
            if (lightness < 0.5) saturation = del_Max / (var_Max + var_Min); else saturation = del_Max / (2 - var_Max - var_Min);
            double del_R = (((var_Max - var_R) / 6) + (del_Max / 2)) / del_Max;
            double del_G = (((var_Max - var_G) / 6) + (del_Max / 2)) / del_Max;
            double del_B = (((var_Max - var_B) / 6) + (del_Max / 2)) / del_Max;
            if (var_R == var_Max) hue = del_B - del_G; else if (var_G == var_Max) hue = ((double) 1 / 3) + del_R - del_B; else if (var_B == var_Max) hue = ((double) 2 / 3) + del_G - del_R;
            if (hue < 0) hue += 1;
            if (hue > 1) hue -= 1;
        }
    }
