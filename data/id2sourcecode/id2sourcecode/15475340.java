    @Override
    public void write(GL gl, String[] lines, Color color, float size, byte align, float minX, float maxX, float minY, float maxY) {
        final float charHeight = size;
        final float charWidth = size * aspect;
        tex.activate(gl);
        gl.glBegin(GL.GL_QUADS);
        gl.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        switch(align) {
            case Font.ALIGN_CENTER:
                {
                    final float center = (minX + maxX) / 2;
                    float top = maxY;
                    for (String line : lines) {
                        float left = center - (line.length() * charWidth) / 2;
                        float bottom = top - charHeight;
                        for (byte b : line.getBytes()) {
                            float right = left + charWidth;
                            float x0 = (b & 15) * sizeX;
                            float y1 = ((b & 240) >> 4) * sizeY;
                            float x1 = x0 + sizeX;
                            float y0 = y1 + sizeY;
                            gl.glTexCoord2f(x0, y0);
                            gl.glVertex2f(left, bottom);
                            gl.glTexCoord2f(x0, y1);
                            gl.glVertex2f(left, top);
                            gl.glTexCoord2f(x1, y1);
                            gl.glVertex2f(right, top);
                            gl.glTexCoord2f(x1, y0);
                            gl.glVertex2f(right, bottom);
                            left += charWidth;
                        }
                        top -= charHeight;
                    }
                    break;
                }
            case Font.ALIGN_RIGHT:
                {
                    final float lright = maxX;
                    float top = maxY;
                    for (String line : lines) {
                        float left = lright - (line.length() * charWidth);
                        float bottom = top - charHeight;
                        for (byte b : line.getBytes()) {
                            float right = left + charWidth;
                            float x0 = (b & 15) * sizeX;
                            float y1 = ((b & 240) >> 4) * sizeY;
                            float x1 = x0 + sizeX;
                            float y0 = y1 + sizeY;
                            gl.glTexCoord2f(x0, y0);
                            gl.glVertex2f(left, bottom);
                            gl.glTexCoord2f(x0, y1);
                            gl.glVertex2f(left, top);
                            gl.glTexCoord2f(x1, y1);
                            gl.glVertex2f(right, top);
                            gl.glTexCoord2f(x1, y0);
                            gl.glVertex2f(right, bottom);
                            left += charWidth;
                        }
                        top -= charHeight;
                    }
                    break;
                }
            default:
                {
                    final float lleft = minX;
                    float top = maxY;
                    for (String line : lines) {
                        float left = lleft;
                        float bottom = top - charHeight;
                        for (byte b : line.getBytes()) {
                            float right = left + charWidth;
                            float x0 = (b & 15) * sizeX;
                            float y1 = ((b & 240) >> 4) * sizeY;
                            float x1 = x0 + sizeX;
                            float y0 = y1 + sizeY;
                            gl.glTexCoord2f(x0, y0);
                            gl.glVertex2f(left, bottom);
                            gl.glTexCoord2f(x0, y1);
                            gl.glVertex2f(left, top);
                            gl.glTexCoord2f(x1, y1);
                            gl.glVertex2f(right, top);
                            gl.glTexCoord2f(x1, y0);
                            gl.glVertex2f(right, bottom);
                            left += charWidth;
                        }
                        top -= charHeight;
                    }
                    break;
                }
        }
        gl.glEnd();
    }
