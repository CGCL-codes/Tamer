package net.laubenberger.bogatyr.view.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import net.laubenberger.bogatyr.helper.HelperLog;
import net.laubenberger.bogatyr.misc.Activatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an extended JCheckBox.
 *
 * @author Stefan Laubenberger
 * @version 0.9.2 (20100611)
 * @since 0.2.0
 */
public class CheckBox extends JCheckBox implements Activatable {

    private static final long serialVersionUID = -6439735629199643683L;

    private static final Logger log = LoggerFactory.getLogger(CheckBox.class);

    private boolean isNotActive;

    public CheckBox() {
        super();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor());
    }

    public CheckBox(final Action action) {
        super(action);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(action));
    }

    public CheckBox(final Icon icon, final boolean selected) {
        super(icon, selected);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(icon, selected));
    }

    public CheckBox(final Icon icon) {
        super(icon);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(icon));
    }

    public CheckBox(final String text, final boolean selected) {
        super(text, selected);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(text, selected));
    }

    public CheckBox(final String text, final Icon icon, final boolean selected) {
        super(text, icon, selected);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(text, icon, selected));
    }

    public CheckBox(final String text, final Icon icon) {
        super(text, icon);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(text, icon));
    }

    public CheckBox(final String text) {
        super(text);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(text));
    }

    public CheckBox(final boolean isSelected) {
        super();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(isSelected));
        setSelected(isSelected);
    }

    public CheckBox(final boolean isSelected, final Action action) {
        this(action);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(isSelected, action));
        setSelected(isSelected);
    }

    public CheckBox(final String text, final boolean isSelected, final String toolTip) {
        this(text, isSelected);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(text, isSelected, toolTip));
        setToolTipText(toolTip);
    }

    public CheckBox(final boolean isSelected, final String toolTip) {
        this();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(isSelected, toolTip));
        setSelected(isSelected);
        setToolTipText(toolTip);
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        if (!isNotActive) {
            super.setEnabled(isEnabled);
        }
    }

    @Override
    public boolean isActive() {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart());
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(!isNotActive));
        return !isNotActive;
    }

    @Override
    public void setActive(final boolean isActive) {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(isActive));
        if (isActive) {
            isNotActive = !isActive;
            setEnabled(isActive);
        } else {
            setEnabled(isActive);
            isNotActive = !isActive;
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }
}
