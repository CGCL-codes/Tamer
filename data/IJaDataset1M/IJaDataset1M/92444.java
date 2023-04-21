package org.sodbeans.controller.impl.readers;

import java.awt.Point;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.awt.MenuBar;
import org.sodbeans.controller.impl.processors.MenuItemProcessor;
import org.sodbeans.controller.impl.processors.NullProcessor;
import org.sodbeans.phonemic.SpeechProcessor;

/**
 *  This reads JMenuItem objects as they are passed to the screen reader.
 * 
 * @author Andreas Stefik
 */
public class JMenuItemReader extends AbstractScreenReader {

    private JMenuItem menuItem = null;

    @Override
    protected SpeechProcessor getMenuEventProcessor() {
        if (menuItem == null) return new NullProcessor();
        MenuItemProcessor proc = new MenuItemProcessor();
        proc.setText(menuItem.getText());
        proc.setEnabled(menuItem.isEnabled());
        proc.setAccelerator(menuItem.getAccelerator());
        proc.setMenu(menuItem.getParent() instanceof MenuBar);
        proc.setMnemonic(menuItem.getMnemonic());
        proc.setHasSubmenu(menuItem.getSubElements().length > 0);
        if (menuItem instanceof JCheckBoxMenuItem) {
            JCheckBoxMenuItem jc = (JCheckBoxMenuItem) menuItem;
            proc.setCheckable(true);
            proc.setChecked(jc.isSelected());
        }
        return proc;
    }

    public void setObject(Object object) {
        menuItem = (JMenuItem) object;
    }

    @Override
    public void magnify() {
        if (magnifier.isStarted()) {
            try {
                Point loc = menuItem.getLocationOnScreen();
                magnifier.setFocusCenter(loc.x, loc.y);
            } catch (Exception e) {
            }
        }
    }
}
