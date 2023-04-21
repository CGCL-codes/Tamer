package org.kalypso.nofdpidss.ui.view.common.navigation.projectmanager.state.utils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.kalypso.contribs.eclipse.core.runtime.ExceptionHelper;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.MyColors;
import org.kalypso.nofdpidss.core.base.gml.model.state.IAction;
import org.kalypso.nofdpidss.core.base.gml.model.state.IModule;
import org.kalypso.nofdpidss.core.base.gml.model.state.IRepresentation;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolState;
import org.kalypso.nofdpidss.core.common.utils.modules.StateUtils.BUTTON_STATE;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree_impl.model.feature.XLinkedFeature_Impl;

/**
 * @author Dirk Kuch
 * @bug see https://bugs.eclipse.org/bugs/show_bug.cgi?id=23837
 */
public class StateButton {

    protected static Cursor m_cursor = null;

    protected final Composite m_parent;

    private final Feature m_module;

    private final Feature m_action;

    private final PoolState m_pool;

    public StateButton(final PoolState pool, final Composite parent, final Feature module, final Feature action) throws CoreException {
        m_pool = pool;
        m_parent = parent;
        m_module = module;
        m_action = action;
        draw();
    }

    private void draw() throws CoreException {
        final Composite cBody = new Composite(m_parent, SWT.None);
        cBody.setLayout(new GridLayout());
        final GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
        data.heightHint = data.minimumHeight = 18;
        data.verticalIndent = -2;
        cBody.setLayoutData(data);
        cBody.setBackground(getBackgroundColor());
        cBody.setToolTipText(getToolTip());
        cBody.addPaintListener(new PaintListener() {

            public void paintControl(final PaintEvent event) {
                final Rectangle rClientArea = cBody.getClientArea();
                final int startX = 8;
                final int startY = 0;
                final int endX = rClientArea.width - 0;
                final int endY = rClientArea.height - 0;
                final int borderWidht = 2;
                try {
                    event.gc.setBackground(getBackgroundColor());
                    event.gc.fillRectangle(startX + borderWidht, startY + borderWidht, endX - 2 * borderWidht, endY - 2 * borderWidht);
                    event.gc.setForeground(getFontColor());
                    event.gc.setFont(MyColors.fState);
                } catch (final CoreException e) {
                    NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                }
                final Point pExtend = event.gc.stringExtent(getButtonText());
                event.gc.drawText(getButtonText(), startX, endY - (endY - startY) / 2 - pExtend.y / 2);
            }
        });
        final BUTTON_STATE state = getButtonState(m_action);
        if (!BUTTON_STATE.eInactive.equals(state)) {
            final StateMouseListener mListener = new StateMouseListener(m_pool, m_action);
            cBody.addMouseListener(mListener);
            cBody.addMouseTrackListener(new MouseTrackAdapter() {

                /**
         * @see org.eclipse.swt.events.MouseTrackAdapter#mouseEnter(org.eclipse.swt.events.MouseEvent)
         */
                @Override
                public void mouseEnter(final MouseEvent e) {
                    final Control control = (Control) e.widget;
                    if (StateButton.m_cursor != null) if (!StateButton.m_cursor.isDisposed()) StateButton.m_cursor.dispose();
                    StateButton.m_cursor = new Cursor(control.getDisplay(), SWT.CURSOR_HAND);
                    control.setCursor(StateButton.m_cursor);
                }
            });
        }
    }

    protected Color getBackgroundColor() throws CoreException {
        final BUTTON_STATE state = getButtonState(m_action);
        final Feature moduleRepresentation = (Feature) m_module.getProperty(IModule.QN_REPRESENTATION);
        final Feature buttonRepresentation = (Feature) moduleRepresentation.getProperty(IRepresentation.QN_BUTTON_REPRESENTATION);
        final RGB rgb;
        if (BUTTON_STATE.eActive.equals(state)) rgb = (RGB) buttonRepresentation.getProperty(GmlConstants.QN_STATE_BOX_LAYOUT_COLOR_BUTTON_BACKGROUND_ACTIVE); else if (BUTTON_STATE.eCurrent.equals(state)) rgb = (RGB) buttonRepresentation.getProperty(GmlConstants.QN_STATE_BOX_LAYOUT_COLOR_BUTTON_BACKGROUND_CURRENT); else if (BUTTON_STATE.eInactive.equals(state)) rgb = (RGB) buttonRepresentation.getProperty(GmlConstants.QN_STATE_BOX_LAYOUT_COLOR_BUTTON_BACKGROUND_INACTIVE); else throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.StateButton_1 + state);
        return MyColors.getInstance().checkForColor(rgb);
    }

    public BUTTON_STATE getButtonState(final Feature fAction) {
        final XLinkedFeature_Impl fState = (XLinkedFeature_Impl) fAction.getProperty(IAction.QN_CURRENT_STATE);
        final String href = fState.getHref();
        try {
            return BUTTON_STATE.getState(href);
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return null;
        }
    }

    protected String getButtonText() {
        return FeatureUtils.getFeatureName(GmlConstants.NS_STATUS, m_action);
    }

    protected Color getFontColor() throws CoreException {
        final BUTTON_STATE state = getButtonState(m_action);
        final Feature moduleRepresentation = (Feature) m_module.getProperty(IModule.QN_REPRESENTATION);
        final Feature buttonRepresentation = (Feature) moduleRepresentation.getProperty(IRepresentation.QN_BUTTON_REPRESENTATION);
        final RGB rgb;
        if (BUTTON_STATE.eActive.equals(state)) rgb = (RGB) buttonRepresentation.getProperty(GmlConstants.QN_STATE_BOX_LAYOUT_COLOR_BUTTON_FONT_ACTIVE); else if (BUTTON_STATE.eCurrent.equals(state)) rgb = (RGB) buttonRepresentation.getProperty(GmlConstants.QN_STATE_BOX_LAYOUT_COLOR_BUTTON_FONT_CURRENT); else if (BUTTON_STATE.eInactive.equals(state)) rgb = (RGB) buttonRepresentation.getProperty(GmlConstants.QN_STATE_BOX_LAYOUT_COLOR_BUTTON_FONT_INACTIVE); else throw ExceptionHelper.getCoreException(IStatus.ERROR, this.getClass(), Messages.StateButton_2 + state);
        return MyColors.getInstance().checkForColor(rgb);
    }

    protected String getToolTip() {
        return Messages.StateButton_0 + " " + getButtonState(m_action).toString();
    }
}
