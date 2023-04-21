package com.jme.scene.shadow;

import java.io.IOException;
import com.jme.util.export.InputCapsule;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.OutputCapsule;
import com.jme.util.export.Savable;

/**
 * <code>ShadowTriangle</code> A class that holds the edge information of a
 * single face (triangle) of an occluder
 * 
 * @author Mike Talbot (some code from a shadow implementation written Jan 2005)
 * @author Joshua Slack
 * @version $Id: ShadowTriangle.java 4131 2009-03-19 20:15:28Z blaine.dev $
 */
public class ShadowTriangle implements Savable {

    /**
     * <code>INVALID_TRIANGLE</code> (int) indicates that an edge is not
     * connected
     */
    public static final int INVALID_TRIANGLE = -1;

    public ShadowEdge edge1 = null;

    public ShadowEdge edge2 = null;

    public ShadowEdge edge3 = null;

    public ShadowTriangle() {
        edge1 = new ShadowEdge(0, 0);
        edge2 = new ShadowEdge(0, 0);
        edge3 = new ShadowEdge(0, 0);
    }

    public void write(JMEExporter e) throws IOException {
        OutputCapsule cap = e.getCapsule(this);
        cap.write(edge1, "edge1", new ShadowEdge(0, 0));
        cap.write(edge2, "edge2", new ShadowEdge(0, 0));
        cap.write(edge3, "edge3", new ShadowEdge(0, 0));
    }

    public void read(JMEImporter e) throws IOException {
        InputCapsule cap = e.getCapsule(this);
        edge1 = (ShadowEdge) cap.readSavable("edge1", new ShadowEdge(0, 0));
        edge2 = (ShadowEdge) cap.readSavable("edge2", new ShadowEdge(0, 0));
        edge3 = (ShadowEdge) cap.readSavable("edge3", new ShadowEdge(0, 0));
    }

    public Class getClassTag() {
        return this.getClass();
    }
}
