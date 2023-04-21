    static Drawable processIcon(Drawable icon_d, CompiledIconShader compiledShader) {
        List<Shader> shaders = compiledShader.shaders;
        Bitmap icon_bitmap = null;
        if (icon_d instanceof BitmapDrawable) {
            BitmapDrawable icon_bd = (BitmapDrawable) icon_d;
            icon_bitmap = icon_bd.getBitmap();
        } else if (icon_d instanceof FastBitmapDrawable) {
            FastBitmapDrawable icon_bd = (FastBitmapDrawable) icon_d;
            icon_bitmap = icon_bd.getBitmap();
        } else return null;
        if (icon_bitmap == null) return null;
        int width = icon_bitmap.getWidth();
        int height = icon_bitmap.getHeight();
        int length = width * height;
        if (length > CompiledIconShader.MAXLENGTH) return null;
        int[] pixels = compiledShader.pixels;
        float[][] icon = compiledShader.icon;
        float[][] buffer = compiledShader.buffer;
        float[][] output = compiledShader.output;
        float icon_average = 0;
        float buffer_average = 0;
        float output_average = 0;
        boolean icon_average_valid = false;
        boolean buffer_average_valid = false;
        boolean output_average_valid = false;
        float[] icon_intensity = compiledShader.icon_intensity;
        float[] buffer_intensity = compiledShader.buffer_intensity;
        ;
        float[] output_intensity = compiledShader.output_intensity;
        boolean icon_intensity_valid = false;
        boolean buffer_intensity_valid = false;
        boolean output_intensity_valid = false;
        icon_bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < length; i++) {
            icon[CHANNEL.BLUE][i] = pixels[i] & 0x000000FF;
            icon[CHANNEL.GREEN][i] = (pixels[i] >> 8) & 0x000000FF;
            icon[CHANNEL.RED][i] = (pixels[i] >> 16) & 0x000000FF;
            icon[CHANNEL.ALPHA][i] = (pixels[i] >> 24) & 0x000000FF;
        }
        float inputValue = 0;
        float[] inputArray = null;
        float[] targetArray = null;
        for (Shader s : shaders) {
            if (s.mode == MODE.NONE) continue;
            if (s.inputMode == INPUT.AVERAGE) {
                switch(s.input) {
                    case IMAGE.ICON:
                        if (!icon_average_valid) {
                            icon_average = getAverage(icon, length);
                            icon_average_valid = true;
                        }
                        inputValue = icon_average;
                        break;
                    case IMAGE.BUFFER:
                        if (!buffer_average_valid) {
                            buffer_average = getAverage(buffer, length);
                            buffer_average_valid = true;
                        }
                        inputValue = buffer_average;
                        break;
                    case IMAGE.OUTPUT:
                        if (!output_average_valid) {
                            output_average = getAverage(output, length);
                            output_average_valid = true;
                        }
                        inputValue = output_average;
                        break;
                }
            }
            if (s.inputMode == INPUT.INTENSITY) {
                switch(s.input) {
                    case IMAGE.ICON:
                        if (!icon_intensity_valid) {
                            getIntensity(icon_intensity, icon, length);
                            icon_intensity_valid = true;
                        }
                        inputArray = icon_intensity;
                        break;
                    case IMAGE.BUFFER:
                        if (!buffer_intensity_valid) {
                            getIntensity(buffer_intensity, buffer, length);
                            buffer_intensity_valid = true;
                        }
                        inputArray = buffer_intensity;
                        break;
                    case IMAGE.OUTPUT:
                        if (!output_intensity_valid) {
                            getIntensity(output_intensity, output, length);
                            output_intensity_valid = true;
                        }
                        inputArray = output_intensity;
                        break;
                }
            }
            if (s.inputMode == INPUT.CHANNEL) {
                switch(s.input) {
                    case IMAGE.ICON:
                        inputArray = icon[s.inputChannel];
                        break;
                    case IMAGE.BUFFER:
                        inputArray = buffer[s.inputChannel];
                        break;
                    case IMAGE.OUTPUT:
                        inputArray = output[s.inputChannel];
                        break;
                }
            }
            if (s.inputMode == INPUT.VALUE) {
                inputValue = s.inputValue;
            }
            if (s.target == IMAGE.BUFFER) {
                targetArray = buffer[s.targetChannel];
            }
            if (s.target == IMAGE.OUTPUT) {
                targetArray = output[s.targetChannel];
            }
            switch(s.mode) {
                case MODE.WRITE:
                    if (s.inputMode == INPUT.AVERAGE || s.inputMode == INPUT.VALUE) {
                        Arrays.fill(targetArray, inputValue);
                    }
                    if (s.inputMode == INPUT.INTENSITY || s.inputMode == INPUT.CHANNEL) {
                        System.arraycopy(inputArray, 0, targetArray, 0, length);
                    }
                    break;
                case MODE.MULTIPLY:
                    if (s.inputMode == INPUT.AVERAGE || s.inputMode == INPUT.VALUE) {
                        for (int i = 0; i < length; i++) targetArray[i] *= inputValue;
                    }
                    if (s.inputMode == INPUT.INTENSITY || s.inputMode == INPUT.CHANNEL) {
                        for (int i = 0; i < length; i++) targetArray[i] *= inputArray[i];
                    }
                    break;
                case MODE.DIVIDE:
                    if (s.inputMode == INPUT.AVERAGE || s.inputMode == INPUT.VALUE) {
                        inputValue = 1 / inputValue;
                        for (int i = 0; i < length; i++) targetArray[i] *= inputValue;
                    }
                    if (s.inputMode == INPUT.INTENSITY || s.inputMode == INPUT.CHANNEL) {
                        for (int i = 0; i < length; i++) targetArray[i] /= inputArray[i];
                    }
                    break;
                case MODE.ADD:
                    if (s.inputMode == INPUT.AVERAGE || s.inputMode == INPUT.VALUE) {
                        for (int i = 0; i < length; i++) targetArray[i] += inputValue;
                    }
                    if (s.inputMode == INPUT.INTENSITY || s.inputMode == INPUT.CHANNEL) {
                        for (int i = 0; i < length; i++) targetArray[i] += inputArray[i];
                    }
                    break;
                case MODE.SUBTRACT:
                    if (s.inputMode == INPUT.AVERAGE || s.inputMode == INPUT.VALUE) {
                        for (int i = 0; i < length; i++) targetArray[i] -= inputValue;
                    }
                    if (s.inputMode == INPUT.INTENSITY || s.inputMode == INPUT.CHANNEL) {
                        for (int i = 0; i < length; i++) targetArray[i] -= inputArray[i];
                    }
                    break;
            }
            switch(s.target) {
                case IMAGE.BUFFER:
                    buffer_average_valid = false;
                    ;
                    buffer_intensity_valid = false;
                    break;
                case IMAGE.OUTPUT:
                    output_average_valid = false;
                    output_intensity_valid = false;
                    break;
            }
        }
        int a, r, g, b;
        for (int i = 0; i < length; i++) {
            a = (int) output[CHANNEL.ALPHA][i];
            r = (int) output[CHANNEL.RED][i];
            g = (int) output[CHANNEL.GREEN][i];
            b = (int) output[CHANNEL.BLUE][i];
            a = a > 255 ? 255 : a < 0 ? 0 : a;
            r = r > 255 ? 255 : r < 0 ? 0 : r;
            g = g > 255 ? 255 : g < 0 ? 0 : g;
            b = b > 255 ? 255 : b < 0 ? 0 : b;
            a <<= 8;
            a |= r;
            a <<= 8;
            a |= g;
            a <<= 8;
            a |= b;
            pixels[i] = a;
        }
        Bitmap.Config c = (icon_bitmap.getConfig() == null) ? Bitmap.Config.ARGB_8888 : icon_bitmap.getConfig();
        Bitmap output_bitmap = Bitmap.createBitmap(pixels, width, height, c);
        output_bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        BitmapDrawable output_bd = new BitmapDrawable(output_bitmap);
        return output_bd;
    }
