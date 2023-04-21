package org.nakedobjects.plugins.dnd.viewer.view.specification;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.CloseViewOption;
import org.nakedobjects.plugins.dnd.viewer.content.OptionFactory;
import org.nakedobjects.plugins.dnd.viewer.content.PerspectiveContent;
import org.nakedobjects.plugins.dnd.viewer.content.ServiceObject;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;

public class ServiceIcon extends Icon {

    private static final int ICON_SIZE;

    private static final int LARGE_ICON_SIZE = 34;

    private static final String LARGE_ICON_SIZE_PROPERTY;

    static {
        LARGE_ICON_SIZE_PROPERTY = Properties.PROPERTY_BASE + "large-icon-size";
        ICON_SIZE = NakedObjectsContext.getConfiguration().getInteger(LARGE_ICON_SIZE_PROPERTY, LARGE_ICON_SIZE);
    }

    public ServiceIcon(final Content content, final ViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        setTitle(new ObjectTitleText(this, Toolkit.getText(ColorsAndFonts.TEXT_ICON)));
        setSelectedIcon(new IconGraphic(this, ICON_SIZE));
        setVertical(true);
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_CONTENT));
        OptionFactory.addObjectMenuOptions(getContent().getNaked(), options);
    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {
        options.setColor(Toolkit.getColor(ColorsAndFonts.COLOR_MENU_VIEW));
        options.add(new CloseViewOption() {

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                final PerspectiveContent parent = (PerspectiveContent) view.getParent().getContent();
                final PerspectiveEntry perspective = parent.getPerspective();
                final ServiceObject serviceContent = (ServiceObject) view.getContent();
                final NakedObject element = serviceContent.getObject();
                perspective.removeFromServices(element);
                super.execute(workspace, view, at);
            }
        });
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        final View dragOverlay = Toolkit.getViewFactory().getContentDragSpecification().createView(getContent(), null);
        return new ContentDrag(this, drag.getLocation(), dragOverlay);
    }
}
