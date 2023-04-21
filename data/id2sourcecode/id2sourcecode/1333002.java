    public static void copyBuffers(Framebuffer read, int readTarget, Framebuffer write, int writeTarget) {
        GL2 gl = Graphics.getGL();
        gl.glBindFramebuffer(GL2.GL_READ_FRAMEBUFFER, read.framebufferId);
        gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, write.framebufferId);
        gl.glReadBuffer(GL2.GL_COLOR_ATTACHMENT0 + readTarget);
        gl.glDrawBuffer(GL2.GL_COLOR_ATTACHMENT0 + writeTarget);
        gl.glBlitFramebuffer(0, 0, read.width, read.height, 0, 0, write.width, write.height, GL2.GL_COLOR_BUFFER_BIT, GL2.GL_LINEAR);
        gl.glBlitFramebuffer(0, 0, read.width, read.height, 0, 0, write.width, write.height, GL2.GL_DEPTH_BUFFER_BIT, GL2.GL_LINEAR);
        gl.glBindFramebuffer(GL2.GL_READ_FRAMEBUFFER, 0);
        gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, 0);
    }
