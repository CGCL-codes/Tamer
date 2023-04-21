package org.gvsig.graph;

import java.util.logging.Logger;
import org.gvsig.graph.gui.wizard.servicearea.AbstractPointsModel;

public class ServiceAreaController {

    private AbstractPointsModel model;

    private boolean fusionAreas;

    private boolean ringAreas;

    private boolean compactAreas;

    private Logger logger;

    public ServiceAreaController() {
        this.setModel(null);
        this.setFusionAreas(false);
        this.setCompactAreas(false);
        this.setRingAreas(false);
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public ServiceAreaController(AbstractPointsModel model, boolean fusionArea, boolean ringAreas, boolean compactAreas) {
        this.setModel(model);
        this.setFusionAreas(fusionAreas);
        this.setRingAreas(ringAreas);
        this.setCompactAreas(compactAreas);
    }

    public void setModel(AbstractPointsModel model) {
        this.model = model;
    }

    public AbstractPointsModel getModel() {
        return this.model;
    }

    public void setFusionAreas(boolean fusionAreas) {
        this.fusionAreas = fusionAreas;
    }

    public boolean getFusionAreas() {
        return this.fusionAreas;
    }

    public void setRingAreas(boolean ringAreas) {
        this.ringAreas = ringAreas;
    }

    public boolean getRingAreas() {
        return this.ringAreas;
    }

    public void setCompactAreas(boolean compactAreas) {
        this.compactAreas = compactAreas;
    }

    public boolean getCompactAreas() {
        return this.compactAreas;
    }

    public void solve() {
        this.logger.info("Resolviendo:\n" + this.model + "\n" + "Fusionar Areas: " + this.fusionAreas + "\n" + "Areas anillo: " + this.ringAreas + "\n" + "Areas compactas: " + this.compactAreas);
    }
}
