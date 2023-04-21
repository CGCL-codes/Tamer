package net.sf.rcpforms.widgetwrapper.wrapper.UNSORTED.bind;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * SWT binding misses an observable value for text of StyledText. This is more ore less a copy of
 * TextObservableValue with adaption to StyleText.
 * 
 * @since it4
 * @author spicherc
 * @version 1.0
 * 
 * @deprecated use Beanlike binding via RCPBeanControls
 */
@SuppressWarnings("restriction")
@Deprecated
public class StyledTextTextObservableValue extends AbstractSWTVetoableValue {

    /**
     * {@link StyledText} widget that this is being observed.
     */
    private final StyledText m_styledText;

    /**
     * Flag to track when the model is updating the widget. When <code>true</code> the handlers for
     * the SWT events should not process the event as this would cause an infinite loop.
     */
    private boolean m_updating = false;

    /**
     * SWT event that on firing this observable will fire change events to its listeners.
     */
    private final int m_updateEventType;

    /**
     * Valid types for the {@link #m_updateEventType}.
     */
    private static final int[] validUpdateEventTypes = new int[] { SWT.Modify, SWT.FocusOut, SWT.None };

    /**
     * Previous value of the Text.
     */
    private String oldValue;

    private final Listener updateListener = new Listener() {

        public void handleEvent(final Event event) {
            if (!m_updating) {
                final String newValue = m_styledText.getText();
                if (!newValue.equals(oldValue)) {
                    fireValueChange(Diffs.createValueDiff(oldValue, newValue));
                    oldValue = newValue;
                }
            }
        }
    };

    private final VerifyListener verifyListener;

    /**
     * Constructs a new instance bound to the given <code>text</code> widget and configured to fire
     * change events to its listeners at the time of the <code>updateEventType</code>.
     * 
     * @param text
     * @param updateEventType SWT event constant as to what SWT event to update the model in
     *            response to. Appropriate values are: <code>SWT.Modify</code>,
     *            <code>SWT.FocusOut</code>, <code>SWT.None</code>.
     * @throws IllegalArgumentException if <code>updateEventType</code> is an incorrect type.
     */
    public StyledTextTextObservableValue(final StyledText styledText, final int updateEventType) {
        this(SWTObservables.getRealm(styledText.getDisplay()), styledText, updateEventType);
    }

    /**
     * Constructs a new instance.
     * 
     * @param realm can not be <code>null</code>
     * @param text
     * @param updateEventType
     */
    public StyledTextTextObservableValue(final Realm realm, final StyledText styledText, final int updateEventType) {
        super(realm, styledText);
        boolean eventValid = false;
        for (int i = 0; !eventValid && i < validUpdateEventTypes.length; i++) {
            eventValid = (updateEventType == validUpdateEventTypes[i]);
        }
        if (!eventValid) {
            throw new IllegalArgumentException("UpdateEventType [" + updateEventType + "] is not supported.");
        }
        this.m_styledText = styledText;
        this.m_updateEventType = updateEventType;
        if (updateEventType != SWT.None) {
            styledText.addListener(updateEventType, updateListener);
        }
        oldValue = styledText.getText();
        verifyListener = new VerifyListener() {

            public void verifyText(final VerifyEvent e) {
                if (!m_updating) {
                    final String currentText = StyledTextTextObservableValue.this.m_styledText.getText();
                    final String newText = currentText.substring(0, e.start) + e.text + currentText.substring(e.end);
                    if (!fireValueChanging(Diffs.createValueDiff(currentText, newText))) {
                        e.doit = false;
                    }
                }
            }
        };
        styledText.addVerifyListener(verifyListener);
    }

    /**
     * Sets the bound {@link Text Text's} text to the passed <code>value</code>.
     * 
     * @param value new value, String expected
     * @see org.eclipse.core.databinding.observable.value.AbstractVetoableValue#doSetApprovedValue(java.lang.Object)
     * @throws ClassCastException if the value is anything other than a String
     */
    @Override
    protected void doSetApprovedValue(final Object value) {
        try {
            m_updating = true;
            m_styledText.setText(value == null ? "" : value.toString());
            oldValue = m_styledText.getText();
        } finally {
            m_updating = false;
        }
    }

    /**
     * Returns the current value of the {@link Text}.
     * 
     * @see org.eclipse.core.databinding.observable.value.AbstractVetoableValue#doGetValue()
     */
    @Override
    public Object doGetValue() {
        return oldValue = m_styledText.getText();
    }

    /**
     * Returns the type of the value from {@link #doGetValue()}, i.e. String.class
     * 
     * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
     */
    public Object getValueType() {
        return String.class;
    }

    @Override
    public void dispose() {
        if (!m_styledText.isDisposed()) {
            if (m_updateEventType != SWT.None) {
                m_styledText.removeListener(m_updateEventType, updateListener);
            }
            m_styledText.removeVerifyListener(verifyListener);
        }
        super.dispose();
    }
}
