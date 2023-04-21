package org.ufacekit.ui.swing.databinding.swing;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.ufacekit.ui.swing.custom.JTabbedPanePage;
import org.ufacekit.ui.swing.databinding.internal.swing.ButtonObservableText;
import org.ufacekit.ui.swing.databinding.internal.swing.ButtonObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.ComboEditableObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.ComboObservableList;
import org.ufacekit.ui.swing.databinding.internal.swing.ComboObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.ComboSingleSelectionObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.ControlObservableIcon;
import org.ufacekit.ui.swing.databinding.internal.swing.ControlObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.LabelObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.ShellObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.SliderObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.SwingProperties;
import org.ufacekit.ui.swing.databinding.internal.swing.TabbedPanePageObservableText;
import org.ufacekit.ui.swing.databinding.internal.swing.TabbedPaneSingleSelectionObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.TableColumnObservableText;
import org.ufacekit.ui.swing.databinding.internal.swing.TextEditableObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.TextObservableValue;
import org.ufacekit.ui.swing.databinding.internal.swing.TitledBorderObservableText;

/**
 * A factory for creating observables for Swing widgets.
 *
 */
public class SwingObservables {

    private static Realm realm = new SwingRealm();

    /**
	 * @return get realm used to sync with UI thread
	 * @since 1.0
	 */
    public static Realm getRealm() {
        return realm;
    }

    private static java.util.List<SwingRealm> realms = new ArrayList<SwingRealm>();

    /**
	 * Returns the realm representing the UI thread for the given currentThread.
	 *
	 * @param currentThread
	 *            the current thread
	 * @return the realm representing the UI thread for the given display
	 * @since 1.0
	 */
    public static Realm getRealm(final Thread currentThread) {
        synchronized (realms) {
            for (Iterator<SwingRealm> it = realms.iterator(); it.hasNext(); ) {
                SwingRealm displayRealm = it.next();
                if (displayRealm.getCurrentThread() == currentThread) {
                    return displayRealm;
                }
            }
            SwingRealm result = new SwingRealm(currentThread);
            realms.add(result);
            return result;
        }
    }

    /**
	 * @param control
	 * @return an observable value tracking the enabled state of the given
	 *         control
	 */
    public static ISwingObservableValue observeEnabled(Container control) {
        return new ControlObservableValue(control, SwingProperties.ENABLED);
    }

    /**
	 * @param control
	 * @return an observable value tracking the visible state of the given
	 *         control
	 */
    public static ISwingObservableValue observeVisible(Container control) {
        return new ControlObservableValue(control, SwingProperties.VISIBLE);
    }

    /**
	 * @param control
	 * @return an observable value tracking the tooltip text of the given
	 *         control
	 */
    public static ISwingObservableValue observeTooltipText(Container control) {
        return new ControlObservableValue(control, SwingProperties.TOOLTIP_TEXT);
    }

    /**
	 * Returns an observable observing the selection attribute of the provided
	 * <code>control</code>. The supported types are:
	 * <ul>
	 * <li>javax.swing.JTree</li>
	 * <li>org.eclipse.swt.widgets.Spinner</li>
	 * <li>org.eclipse.swt.widgets.Button</li>
	 * <li>javax.swing.JComboBox</li>
	 * <li>javax.swing.AbstractButton</li>
	 * <li>javax.swing.JList</li>
	 * <li>javax.swing.JSlider</li>
	 *
	 * </ul>
	 *
	 * @param control
	 * @return observable value
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static ISwingObservableValue observeSelection(Container control) {
        if (control instanceof AbstractButton) {
            return new ButtonObservableValue((AbstractButton) control);
        } else if (control instanceof JSlider) {
            return new SliderObservableValue((JSlider) control);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }

    /**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. The supported types are:
	 * <ul>
	 * <li>org.eclipse.swt.widgets.Text</li>
	 * </ul>
	 *
	 * @param control
	 * @param event
	 *            event type to register for change events
	 * @return observable value
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static ISwingObservableValue observeText(JComponent control, int event) {
        if (control instanceof JTextComponent) {
            return new TextObservableValue((JTextComponent) control, event);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }

    /**
	 * Returns an observable observing the text attribute of the provided
	 * <code>control</code>. The supported types are:
	 * <ul>
	 * <li>javax.swing.JLabel</li>
	 * <li>javax.swing.JComboBox</li>
	 * <li>javax.swing.JFrame</li>
	 * <li>javax.swing.AbstractButton</li>
	 * </ul>
	 *
	 * @param control
	 * @return observable value
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static ISwingObservableValue observeText(Container control) {
        if (control instanceof JLabel) {
            return new LabelObservableValue((JLabel) control);
        } else if (control instanceof JComboBox) {
            return new ComboObservableValue((JComboBox) control, SwingProperties.TEXT);
        } else if (control instanceof JFrame) {
            return new ShellObservableValue((JFrame) control);
        } else if (control instanceof AbstractButton) {
            return new ButtonObservableText((AbstractButton) control);
        } else if (control instanceof JTabbedPanePage) {
            return new TabbedPanePageObservableText((JTabbedPanePage) control);
        } else if (control instanceof JComponent) {
            JComponent component = (JComponent) control;
            if (component.getBorder() instanceof TitledBorder) {
                return observeText((TitledBorder) component.getBorder());
            }
            return new TabbedPanePageObservableText((JTabbedPanePage) control);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }

    /**
	 * Observe the text of a table column
	 *
	 * @param tableColumn
	 *            the table column to observe
	 * @return the observable
	 * @since 1.0
	 */
    public static ISwingObservableValue observeText(TableColumn tableColumn) {
        return new TableColumnObservableText(tableColumn);
    }

    /**
	 * Observe the border text
	 *
	 * @param titledBorder
	 *            the border to observe
	 * @return the observable
	 * @since 1.0
	 */
    public static ISwingObservableValue observeText(TitledBorder titledBorder) {
        return new TitledBorderObservableText(titledBorder);
    }

    /**
	 * Returns an observable observing the items attribute of the provided
	 * <code>control</code>. The supported types are:
	 * <ul>
	 * <li>javax.swing.JComboBox</li>
	 * </ul>
	 *
	 * @param control
	 * @return observable list
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static IObservableList observeItems(Container control) {
        if (control instanceof JComboBox) {
            return new ComboObservableList((JComboBox) control);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }

    /**
	 * Returns an observable observing the single selection index attribute of
	 * the provided <code>control</code>. The supported types are:
	 * <ul>
	 * <li>javax.swing.JComboBox</li>
	 * <li>javax.swing.JTabbedPane</li>
	 * </ul>
	 *
	 * @param control
	 * @return observable value
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static ISwingObservableValue observeSingleSelectionIndex(Container control) {
        if (control instanceof JComboBox) {
            return new ComboSingleSelectionObservableValue((JComboBox) control);
        } else if (control instanceof JTabbedPane) {
            return new TabbedPaneSingleSelectionObservableValue((JTabbedPane) control);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }

    /**
	 * @param control
	 * @return an observable value tracking the foreground color of the given
	 *         control
	 */
    public static ISwingObservableValue observeForeground(Container control) {
        return new ControlObservableValue(control, SwingProperties.FOREGROUND);
    }

    /**
	 * @param control
	 * @return an observable value tracking the background color of the given
	 *         control
	 */
    public static ISwingObservableValue observeBackground(Container control) {
        return new ControlObservableValue(control, SwingProperties.BACKGROUND);
    }

    /**
	 * @param control
	 * @return an observable value tracking the font of the given control
	 */
    public static ISwingObservableValue observeFont(Container control) {
        return new ControlObservableValue(control, SwingProperties.FONT);
    }

    /**
	 * Returns an observable observing the editable attribute of the provided
	 * <code>control</code>. The supported types are:
	 * <ul>
	 * <li>javax.swing.text.JTextComponent</li>
	 * <li>javax.swing.JComboBox</li>
	 * </ul>
	 *
	 * @param control
	 *            the control
	 * @return observable value
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static ISwingObservableValue observeEditable(Container control) {
        if (control instanceof JTextComponent) {
            return new TextEditableObservableValue((JTextComponent) control);
        }
        if (control instanceof JComboBox) {
            return new ComboEditableObservableValue((JComboBox) control);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }

    /**
	 * Observe image attribute of the given control. The supported types are
	 * <ul>
	 * <li>{@link AbstractButton}</li>
	 * <li>{@link JLabel}</li>
	 * <li>{@link JTabbedPanePage}</li>
	 * </ul>
	 *
	 * @param control
	 *            the control
	 * @return the observable
	 * @since 1.0
	 * @throws IllegalArgumentException
	 *             if <code>control</code> type is unsupported
	 */
    public static ISwingObservableValue observeImage(Container control) {
        if (control instanceof AbstractButton) {
            return new ControlObservableIcon((AbstractButton) control);
        }
        if (control instanceof JLabel) {
            return new ControlObservableIcon((JLabel) control);
        }
        if (control instanceof JTabbedPanePage) {
            return new ControlObservableIcon((JTabbedPanePage) control);
        }
        throw new IllegalArgumentException("Widget [" + control.getClass().getName() + "] is not supported.");
    }
}
