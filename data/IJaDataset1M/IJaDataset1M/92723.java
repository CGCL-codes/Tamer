package edu.ucsd.ncmir.bridge.renderable;

import com.jme.curve.PolylineCurve;
import com.jme.math.Vector3f;
import edu.ucsd.ncmir.spl.graphics.renderables.Renderable;
import edu.ucsd.ncmir.spl.graphics.Triplet;
import edu.ucsd.ncmir.spl.graphics.meshables.Contours;
import edu.ucsd.ncmir.spl.graphics.meshables.Meshable;
import edu.ucsd.ncmir.spl.graphics.meshables.TriangleMesh;
import edu.ucsd.ncmir.visualizer.geometry.AbstractNode;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
public class RenderableNode extends AbstractNode {

    public RenderableNode(Renderable renderable) {
        super(renderable.getName());
        Color color = renderable.getColor();
        if (color != null) this.setColor(color);
        if (renderable.hasData()) this.buildGeometry(renderable.getMeshables());
        for (Renderable child : renderable.getChildren()) this.attachChild(new RenderableNode(child));
    }

    private void buildGeometry(ArrayList<Meshable> meshes) {
        for (Meshable mesh : meshes) if (mesh.hasData()) {
            if (mesh instanceof TriangleMesh) {
                TriangleMesh tmesh = (TriangleMesh) mesh;
                Triplet[][] vertices = tmesh.getVertexArray();
                int[][] indices = tmesh.getIndexArray();
                float[][] verts = new float[vertices.length][3];
                float[][] normals = new float[vertices.length][3];
                for (int i = 0; i < vertices.length; i++) {
                    verts[i][0] = (float) vertices[i][0].getU();
                    verts[i][1] = (float) vertices[i][0].getV();
                    verts[i][2] = (float) vertices[i][0].getW();
                    normals[i][0] = (float) vertices[i][1].getU();
                    normals[i][1] = (float) vertices[i][1].getV();
                    normals[i][2] = (float) vertices[i][1].getW();
                }
                super.addMesh(verts, normals, null, indices);
            } else if (mesh instanceof Contours) {
                Contours contours = (Contours) mesh;
                String sname = super.getName();
                int i = 0;
                for (ArrayList<Triplet> c : contours.getContours()) {
                    Vector3f[] contour = new Vector3f[c.size() + 1];
                    int v = 0;
                    for (Triplet t : c) contour[v++] = new Vector3f((float) t.getU(), (float) t.getV(), (float) t.getW());
                    contour[v++] = new Vector3f((float) c.get(0).getU(), (float) c.get(0).getV(), (float) c.get(0).getW());
                    PolylineCurve pc = new PolylineCurve(sname + "-contour-" + i++, contour);
                    super.attachPolyline(pc);
                }
            }
        }
    }

    @Override
    public void initializeGeometry(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fetchGeometry(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
