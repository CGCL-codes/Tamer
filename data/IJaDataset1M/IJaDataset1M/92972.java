package com.pustral.comvey.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.pustral.comvey.client.servlet.ISrvOpinion;
import com.pustral.comvey.shared.ExcNoPrivilege;
import com.pustral.comvey.shared.SessionData;
import com.pustral.comvey.shared.ComveyUtil.Action;
import com.pustral.comvey.shared.ComveyUtil.Table;
import com.pustral.comvey.shared.pojo.VOOpinion;

public class SrvOpinion extends RemoteServiceServlet implements ISrvOpinion {

    @Override
    public void delete(SessionData sessionData, VOOpinion voOpinion) throws ExcNoPrivilege, Exception {
        Action action = Action.DELETE;
        if (!SrvUtil.checkAuthorization(sessionData, Table.STC_CODE, action)) {
            throw new ExcNoPrivilege(Table.STC_CODE, action);
        }
    }

    @Override
    public String[][] getAll(SessionData sessionData) throws ExcNoPrivilege, Exception {
        Action action = Action.SELECT;
        if (!SrvUtil.checkAuthorization(sessionData, Table.STC_CODE, action)) {
            throw new ExcNoPrivilege(Table.STC_CODE, action);
        }
        return null;
    }

    @Override
    public VOOpinion insert(SessionData sessionData, VOOpinion voOpinion) throws ExcNoPrivilege, Exception {
        Action action = Action.INSERT;
        if (!SrvUtil.checkAuthorization(sessionData, Table.STC_CODE, action)) {
            throw new ExcNoPrivilege(Table.STC_CODE, action);
        }
        return null;
    }

    @Override
    public VOOpinion update(SessionData sessionData, VOOpinion voOpinion) throws ExcNoPrivilege, Exception {
        Action action = Action.UPDATE;
        if (!SrvUtil.checkAuthorization(sessionData, Table.STC_CODE, action)) {
            throw new ExcNoPrivilege(Table.STC_CODE, action);
        }
        return null;
    }
}
