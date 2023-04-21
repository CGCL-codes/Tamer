        private void display(GLAutoDrawable drawable, Point p) {
            if (renderer == null) {
                renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 10), true, true);
            }
            Neuron[] n = cells[p.x][p.y];
            renderer.begin3DRendering();
            renderer.setColor(0, 0, 1, 0.8f);
            float s1 = n[0].getState(), s2 = n[1].getState();
            float avg = (s1 + s2) / 2;
            String s = String.format("%5.3f", avg);
            Rectangle2D rect = renderer.getBounds(s);
            renderer.draw3D(s, p.x, p.y, 0, .7f);
            renderer.end3DRendering();
            GL gl = drawable.getGL();
            gl.glRectf(p.x, p.y - 2, p.x + (float) rect.getWidth() * avg * .7f, p.y - 1);
        }
