package com.jrandrews.statemachine.demo;

import com.jrandrews.statemachine.BasicStateUpdate;
import com.jrandrews.statemachine.Context;

/**
 * Updates the alarm state.  Requires one String parameter. Valid
 * values are "on" and "off".  Other values are ignored.
 * @author JAndrews
 */
public class SetAlarmUpdate extends BasicStateUpdate {

    public void invoke(Context ctx) {
        super.invoke(ctx);
        String state = getStringParameter(0);
        if ("on".equals(state)) {
            Model.getInstance().setAlarm(true);
        } else if ("off".equals(state)) {
            Model.getInstance().setAlarm(false);
        }
    }
}
