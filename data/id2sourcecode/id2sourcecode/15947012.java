    public void loadTextures(Texture[] textures, int[] ids) {
        IntBuffer glIds = BufferUtils.createIntBuffer(ids.length);
        GL11.glGenTextures(glIds);
        glIds.get(ids);
        for (int i = 0; i < ids.length; i++) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ids[i]);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
            Image image = textures[i].getImage();
            byte[] data = image.getData();
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
            buffer.put(data);
            buffer.rewind();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, 3, image.getWidth(), image.getHeight(), 0, getGLType(image.getChannelLayout()), GL11.GL_UNSIGNED_BYTE, buffer);
        }
    }
