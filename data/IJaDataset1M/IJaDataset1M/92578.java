package web.epp.action.org;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.pub.action.EppBaseAction;
import com.hk.bean.CmpOrgMsg;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.HkUtil;
import com.hk.frame.util.page.PageSupport;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpOrgMsgService;
import com.hk.svr.pub.Err;

@Component("/epp/web/org/msg")
public class MsgAction extends EppBaseAction {

    @Autowired
    private CmpOrgMsgService cmpOrgMsgService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        this.loadOrgInfo(req);
        if (this.isForwardPage(req)) {
            this.setCmpNavInfo(req);
            return this.getWebPath("mod/2/0/org/msg/create.jsp");
        }
        String imgv = req.getString("imgv");
        String imgv_session = (String) req.getSessionValue(HkUtil.CLOUD_IMAGE_AUTH);
        if (!DataUtil.eqNotNull(imgv, imgv_session)) {
            return this.onError(req, Err.IMG_VALIDATE_CODE_ERROR, "createerror", null);
        }
        long companyId = req.getLong("companyId");
        CmpOrgMsg cmpMsg = new CmpOrgMsg();
        cmpMsg.setName(req.getHtmlRow("name"));
        cmpMsg.setTel(req.getHtmlRow("tel"));
        cmpMsg.setContent(req.getHtmlRow("content"));
        cmpMsg.setCompanyId(companyId);
        cmpMsg.setCreateTime(new Date());
        cmpMsg.setIp(req.getRemoteAddr());
        cmpMsg.setOrgId(req.getLong("orgId"));
        int code = cmpMsg.validate();
        if (code != Err.SUCCESS) {
            return this.onError(req, code, "createerror", null);
        }
        this.cmpOrgMsgService.createCmpOrgMsg(cmpMsg);
        req.setSessionText("epp.cmpmsg.create.success");
        return this.onSuccess2(req, "createok", null);
    }

    /**
	 * 留言列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-7-9
	 */
    public String list(HkRequest req, HkResponse resp) throws Exception {
        this.loadOrgInfo(req);
        if (!this.isCanAdminOrg(req)) {
            return null;
        }
        long orgId = req.getLong("orgId");
        long companyId = req.getLong("companyId");
        PageSupport page = req.getPageSupport(20);
        page.setTotalCount(this.cmpOrgMsgService.countCmpOrgMsgByCompanyIdAndOrgId(companyId, orgId));
        List<CmpOrgMsg> list = this.cmpOrgMsgService.getCmpOrgMsgListByCompanyIdAndOrgId(companyId, orgId, page.getBegin(), page.getSize());
        req.setAttribute("list", list);
        return this.getWebPath("mod/2/0/org/msg/list.jsp");
    }

    /**
	 * 删除留言
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-7-9
	 */
    public String del(HkRequest req, HkResponse resp) throws Exception {
        this.loadOrgInfo(req);
        if (!this.isCanAdminOrg(req)) {
            return null;
        }
        long orgId = req.getLong("orgId");
        long companyId = req.getLong("companyId");
        long oid = req.getLong("oid");
        CmpOrgMsg cmpOrgMsg = this.cmpOrgMsgService.getCmpOrgMsg(companyId, oid);
        if (cmpOrgMsg == null || cmpOrgMsg.getOrgId() != orgId) {
            return null;
        }
        this.cmpOrgMsgService.deleteCmpOrgMsg(companyId, oid);
        this.setDelSuccessMsg(req);
        return null;
    }
}
