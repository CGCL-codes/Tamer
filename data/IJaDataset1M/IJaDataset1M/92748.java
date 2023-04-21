package com.global360.sketchpadbpmn.Modes;

import java.awt.geom.Point2D;
import com.global360.sketchpadbpmn.SketchManager;
import com.global360.sketchpadbpmn.WorkflowCanvas;
import com.global360.sketchpadbpmn.documents.idmanager.SketchpadIdException;
import com.global360.sketchpadbpmn.graphic.BPMNFlowGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNFlowType;

/**
 * @author andya
 *
 */
public class CreateSequenceFlowMode extends CreateConnectorMode {

    /**
   * 
   */
    public CreateSequenceFlowMode(SketchManager owner, WorkflowCanvas canvas) {
        super(owner, canvas, CanvasMode.CONNECT_SEQUENCE_MODE);
    }

    @Override
    protected BPMNFlowGraphic createGraphic(Point2D firstPoint, Point2D secondPoint) throws SketchpadIdException {
        BPMNFlowGraphic result = new BPMNFlowGraphic(makeCursorGraphicId(), getCreateModeFlowType(), firstPoint, secondPoint);
        return result;
    }

    @Override
    protected BPMNFlowType getCreateModeFlowType() {
        return new BPMNFlowType(BPMNFlowType.SEQUENCE_FLOW);
    }
}
