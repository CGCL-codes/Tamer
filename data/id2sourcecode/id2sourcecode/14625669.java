    public void setXForm(AxesObject ax) {
        double zmin = ax.x_zmin, zmax = ax.x_zmax;
        if (isGL2PS) {
            if (!isFirst) GL2PS.gl2psEndViewport();
            isFirst = false;
            int[] viewport = new int[4];
            gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0);
            GL2PS.gl2psBeginViewport(viewport);
        }
        xForm = ax.x_render;
        xZ1 = zmin - (zmax - zmin) / 2;
        xZ2 = zmax + (zmax - zmin) / 2;
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glScaled(1, 1, -1);
        gl.glMultMatrixd(ax.x_mat1.getData(), 0);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, d.getWidth(), d.getHeight(), 0, xZ1, xZ2);
        gl.glMultMatrixd(ax.x_mat2.getData(), 0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        sx = ax.sx;
        sy = ax.sy;
        sz = ax.sz;
    }
