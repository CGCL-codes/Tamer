package net.sourceforge.advantag.listener;

import java.util.EventListener;
import net.sourceforge.advantag.event.EditEvent;

/**
 * @author Kristof Vanhaeren
 */
public interface EditEventListener extends EventListener {

    public void editEventOccurred(EditEvent event);
}
