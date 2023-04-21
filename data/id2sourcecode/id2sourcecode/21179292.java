        Gradient(int width, int height, int inColor, int outColor) {
            w = width;
            h = height;
            in = inColor;
            out = outColor;
            x = -1;
            y = 0;
            halfW = (w + 1) / 2;
            halfH = (h + 1) / 2;
            max = halfW * halfW + halfH * halfH;
            yMinusHalfH2 = (y - halfH) * (y - halfH);
        }
