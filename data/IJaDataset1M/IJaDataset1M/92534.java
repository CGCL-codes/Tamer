package self.amigo.editor.tool;

import javax.swing.Action;
import javax.swing.Icon;
import self.amigo.elem.ContextMenuView;
import self.swing.IconUtils;

public class ContextMenuTool extends ABasicCreationTool {

    public ContextMenuTool() {
        userInterfaceSink.putValue(Action.NAME, "Context Menu");
        Icon gif = IconUtils.getIconWithDescription("/self/amigo/images/ContextMenu.gif", "Context Menu Tool");
        userInterfaceSink.putValue(Action.SMALL_ICON, gif);
    }

    protected void constructElement() {
        view = new ContextMenuView();
    }
}
