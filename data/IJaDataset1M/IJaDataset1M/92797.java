package org.adempiere.webui.part;

import org.zkoss.zk.ui.Component;

/**
 * 
 * @author Low Heng Sin
 *
 */
public interface UIPart {

    public Component createPart(Object parent);

    public Component getComponent();
}
