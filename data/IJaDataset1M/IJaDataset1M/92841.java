package au.edu.qut.yawl.editor.elements.model;

import java.awt.geom.Point2D;

public abstract class YAWLCondition extends YAWLVertex {

    public YAWLCondition() {
        super();
    }

    public YAWLCondition(Point2D startPoint) {
        super(startPoint);
    }
}
