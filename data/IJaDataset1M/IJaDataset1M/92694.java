package de.lotk.webftp.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import de.lotk.webftp.Constants;
import de.lotk.webftp.bean.SessionContainer;
import de.lotk.webftp.business.FtpClientConnection;

/**
 * Action, die ein Verzeichnis auf dem FTP-Server loescht.
 * 
 * @author Stephan Sann
 * @version 1.0
 */
public final class RemoveDirAction extends BaseFtpAction {

    /**
   * Process the specified HTTP request, and create the corresponding HTTP
   * response (or forward to another web component that will create it).
   * Return an <code>ActionForward</code> instance describing where and how
   * control should be forwarded, or <code>null</code> if the response has
   * already been completed.
   *
   * @param mapping The ActionMapping used to select this instance
   * @param form The optional ActionForm bean for this request (if any)
   * @param request The HTTP request we are processing
   * @param response The HTTP response we are creating
   *
   * @exception Exception if business logic throws an exception
   */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String removeDir = request.getParameter(Constants.PARAM_REMOVE_DIR);
        if ((removeDir != null) && (removeDir.length() >= 1)) {
            HttpSession session = request.getSession();
            SessionContainer sessionContainer = (SessionContainer) session.getAttribute(Constants.ATTRIB_SESSION_CONTAINER);
            FtpClientConnection ftpClientConnection = sessionContainer.getFtpClientConnection();
            String remoteDir = this.assembleRemoteFileName(sessionContainer, removeDir);
            try {
                ftpClientConnection.verifyConnection(sessionContainer.getLoginData());
                boolean erfolg = ftpClientConnection.removeDirRecursively(remoteDir);
                if (!erfolg) {
                    throw (new Exception("Verzeichnis konnte nicht geloescht werden."));
                }
            } catch (Exception e) {
                e.printStackTrace();
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR, (new ActionError("error.ftpClientConnection.removeDir.failed")));
                saveErrors(request, errors);
                return (mapping.getInputForward());
            }
        }
        return (mapping.findForward("directory-listing"));
    }
}
