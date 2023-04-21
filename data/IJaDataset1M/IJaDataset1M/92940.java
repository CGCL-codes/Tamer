package ca.ucalgary.cpsc.agilePlanner.cards.celleditorlocator;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Text;
import ca.ucalgary.cpsc.agilePlanner.cards.CardConstants;
import ca.ucalgary.cpsc.agilePlanner.cards.celleditorlocator.listener.StoryCardLiveTextEditorListener;
import ca.ucalgary.cpsc.agilePlanner.cards.editpart.StoryCardEditPart;
import ca.ucalgary.cpsc.agilePlanner.cards.model.StoryCardModel;

public class StoryCardBestCaseLabelCellEditorLocation extends ca.ucalgary.cpsc.agilePlanner.util.Object implements CellEditorLocator {

    private StoryCardModel storyModel;

    private StoryCardEditPart storyEditPart;

    public StoryCardBestCaseLabelCellEditorLocation(StoryCardModel model, StoryCardEditPart editPart) {
        this.storyModel = model;
        this.storyEditPart = editPart;
    }

    public void relocate(CellEditor celleditor) {
        ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) storyEditPart.getRoot();
        double ratio = root.getZoomManager().getZoom();
        celleditor.addListener(new StoryCardLiveTextEditorListener(storyModel, celleditor, CardConstants.BestCaseLabel));
        Text text = (Text) celleditor.getControl();
        text.setFont(new Font(null, storyEditPart.getStoryCardFont(), (storyEditPart.getStoryCardFontSize() * ((int) ratio)), 0));
        int xRelative = storyEditPart.getBestCaseLabel().getLocation().x - storyEditPart.getFigure().getBounds().x;
        int yRelative = storyEditPart.getBestCaseLabel().getLocation().y - storyEditPart.getFigure().getBounds().y;
        Rectangle rect = storyEditPart.getFigure().getClientArea();
        storyEditPart.getFigure().translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
        if (storyEditPart.getStoryCardFontSize() < 12) text.setBounds(rect.x + xRelative, rect.y + yRelative, ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardEstimateTextBoxWidth : CardConstants.WindowsStoryCardEstimateTextBoxWidth), ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize()) : CardConstants.WindowsStoryCardEstimateTextBoxHeight)); else text.setBounds(rect.x + xRelative, rect.y + yRelative, ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? storyEditPart.getStoryCardFontSize() * 2 : storyEditPart.getStoryCardFontSize() * 2), ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize()) : (this.storyEditPart.getStoryCardFontSize())));
    }
}
