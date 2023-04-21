    private void drawScaledTintedImage(Graphics g, IOpenGL gl, ITexture texture, ColorRGBA color, int x, int y, int width, int height) {
        x += g.getTranslation().getX();
        y += g.getTranslation().getY();
        gl.enableTexture2D(true);
        {
        }
        colorBuffer.clear();
        colorBuffer.put(color.r).put(color.g).put(color.b).put(color.a);
        colorBuffer.rewind();
        texture.bind();
        GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, colorBuffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);
        gl.startQuads();
        float startY = 0.0f;
        float startX = 0.0f;
        float endY = 1.0f;
        float endX = 1.0f;
        int rWidth = width;
        int rHeight = height;
        Rectangle clipSpace = g.getClipSpace();
        if (x < clipSpace.getX()) {
            rWidth -= clipSpace.getX() - x;
            startX = (float) (clipSpace.getX() - x) / (float) width;
            x = clipSpace.getX();
        }
        if (x + rWidth > clipSpace.getX() + clipSpace.getWidth()) {
            rWidth = clipSpace.getX() + clipSpace.getWidth() - x;
            endX = (float) rWidth / (float) width;
        }
        if (y < clipSpace.getY()) {
            rHeight -= clipSpace.getY() - y;
            endY = (float) rHeight / (float) height;
            y = clipSpace.getY();
        }
        if (y + rHeight > clipSpace.getY() + clipSpace.getHeight()) {
            rHeight = clipSpace.getY() + clipSpace.getHeight() - y;
            startY = (float) (height - rHeight) / (float) height;
        }
        gl.texCoord(startX, endY);
        gl.vertex(x, y);
        gl.texCoord(startX, startY);
        gl.vertex(x, rHeight + y);
        gl.texCoord(endX, startY);
        gl.vertex(rWidth + x, rHeight + y);
        gl.texCoord(endX, endY);
        gl.vertex(rWidth + x, y);
        gl.end();
        gl.enableTexture2D(false);
    }
