package com.peterhi.classroom;

import java.awt.MultipleGradientPaint.CycleMethod;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import com.peterhi.runtime.Util;

public class MyJoglTriangleFragmentationExample extends MyJoglExampleTemplate {

    public static void main(String[] args) {
        new MyJoglTriangleFragmentationExample().run();
    }

    protected void onDisplay(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -5.0f);
        gl.glRotatef(0.0f, 0.0f, 1.0f, 0.0f);
        double[] tri = new double[] { 2.0f, 1.0f, 1.0f, -1.0f, -2.0f, -1.0f };
        double[][] ds = Util.fragment(tri[0], tri[1], tri[2], tri[3], tri[4], tri[5], 0.1);
        gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < ds.length; i++) {
            double frac;
            frac = Util.distFraction(0.0f, 0.0f, 1, ds[i][0], ds[i][1]);
            gl.glColor3d(frac, 1 - frac, 0.0);
            gl.glVertex3f((float) ds[i][0], (float) ds[i][1], 0.0f);
            frac = Util.distFraction(0.0f, 0.0f, 1, ds[i][2], ds[i][3]);
            gl.glColor3d(frac, 1 - frac, 0.0);
            gl.glVertex3f((float) ds[i][2], (float) ds[i][3], 0.0f);
            frac = Util.distFraction(0.0f, 0.0f, 1, ds[i][4], ds[i][5]);
            gl.glColor3d(frac, 1 - frac, 0.0);
            gl.glVertex3f((float) ds[i][4], (float) ds[i][5], 0.0f);
        }
        gl.glEnd();
    }

    private void paintTri(GLAutoDrawable d, double[] tri, int index, int depth) {
        if (depth == 0) {
            return;
        }
        GL gl = d.getGL();
        double[] buf = new double[36];
        Util.tri1to6(tri[index + 0], tri[index + 1], tri[index + 2], tri[index + 3], tri[index + 4], tri[index + 5], buf);
        gl.glBegin(GL.GL_TRIANGLES);
        for (int i = 0; i < buf.length; i += 2) {
            float rate = 1 - (float) i / (float) buf.length / 2;
            gl.glColor3f(rate, rate, rate);
            gl.glVertex3f((float) buf[i + 0], (float) buf[i + 1], (float) 0.0f);
        }
        gl.glEnd();
        for (int i = 0; i < buf.length; i += 6) {
            paintTri(d, buf, i, depth - 1);
        }
    }
}
