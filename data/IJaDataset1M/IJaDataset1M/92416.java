package net.sf.doolin.app.svena.gui.action;

import net.sf.doolin.app.svena.gui.bean.AuthzBean;
import net.sf.doolin.gui.action.ActionContext;
import org.apache.commons.lang.Validate;

public class AuthzPathAddAction extends AbstractAuthzAction {

    @Override
    protected void doExecute(ActionContext context, AuthzBean authzModel) throws Exception {
        String path = authzModel.getPath();
        Validate.notEmpty(path);
        authzModel.addPath(path);
        authzModel.setPath(null);
        authzModel.setDirty(true);
    }
}
