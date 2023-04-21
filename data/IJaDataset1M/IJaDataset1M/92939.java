package org.boxlayout.gui.picking;

import java.awt.event.MouseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.boxlayout.gui.Pickable;
import org.boxlayout.input.pointer.PointerEvent;

public class MoveMode extends AbstractMode {

    Log log = LogFactory.getLog(MoveMode.class);

    public void process(PickingManager ph, Pickable target, PointerEvent event) {
        Pickable lastPicked = ph.getLastPicked();
        if (lastPicked == null) {
            event.setType(PointerEvent.Type.ENTER_ZONE);
            target.triggerPointerEvent(event);
            ph.setLastPicked(target);
        } else if (lastPicked == target) {
        } else {
            event.setType(PointerEvent.Type.ENTER_ZONE);
            target.triggerPointerEvent(event);
            PointerEvent event2 = event.clone();
            event2.setType(PointerEvent.Type.LEAVE_ZONE);
            lastPicked.triggerPointerEvent(event2);
            ph.setLastPicked(target);
        }
    }
}
