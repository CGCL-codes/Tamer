package com.turnengine.client.local.unit.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;
import com.turnengine.client.local.unit.bean.IUnit;
import java.util.List;

/**
 * The Get Units By Group Name Service Async.
 */
@GwtCompatible
public interface GetUnitsByGroupNameServiceAsync extends IGeneratedRemoteServiceAsync {

    void getUnitsByGroupName(long loginId, int instanceId, String name, AsyncCallback<List<IUnit>> calback);
}
