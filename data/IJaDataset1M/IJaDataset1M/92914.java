package org.clark.ace2.core.condact;

import org.clark.ace2.core.Context;

/**
 *
 * @author andy
 */
public class SAME extends AbstractCondAct {

    public SAME() {
        params = new Class[] { String.class, String.class };
    }

    @Override
    protected CondactResponse process(Context ctx, Object... parameters) {
        int flag1 = ctx.getFlagManager().getFlag((String) parameters[0]);
        int flag2 = ctx.getFlagManager().getFlag((String) parameters[1]);
        if (flag1 == flag2) return CondactResponse.OK;
        return CondactResponse.COND_EXIT;
    }
}
