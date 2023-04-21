package jmetest.TutorialGuide;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

/**
 * Started Date: Jul 20, 2004<br><br>
 *
 * Demonstrates making a new TriMesh object from scratch.
 * 
 * @author Jack Lindamood
 */
public class HelloTriMesh extends SimpleGame {

    public static void main(String[] args) {
        HelloTriMesh app = new HelloTriMesh();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    protected void simpleInitGame() {
        TriMesh m = new TriMesh("My Mesh");
        Vector3f[] vertexes = { new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), new Vector3f(1, 1, 0) };
        Vector3f[] normals = { new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 1), new Vector3f(0, 0, 1) };
        ColorRGBA[] colors = { new ColorRGBA(1, 0, 0, 1), new ColorRGBA(1, 0, 0, 1), new ColorRGBA(0, 1, 0, 1), new ColorRGBA(0, 1, 0, 1) };
        Vector2f[] texCoords = { new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(0, 1), new Vector2f(1, 1) };
        int[] indexes = { 0, 1, 2, 1, 2, 3 };
        m.reconstruct(BufferUtils.createFloatBuffer(vertexes), BufferUtils.createFloatBuffer(normals), BufferUtils.createFloatBuffer(colors), TexCoords.makeNew(texCoords), BufferUtils.createIntBuffer(indexes));
        m.setModelBound(new BoundingBox());
        m.updateModelBound();
        rootNode.attachChild(m);
        lightState.setEnabled(false);
    }
}
