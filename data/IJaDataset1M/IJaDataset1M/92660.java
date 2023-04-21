package com.liferay.portal.ejb;

/**
 * <a href="PortletPreferencesHBMUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class PortletPreferencesHBMUtil {

    public static com.liferay.portal.model.PortletPreferences model(PortletPreferencesHBM portletPreferencesHBM) {
        com.liferay.portal.model.PortletPreferences portletPreferences = PortletPreferencesPool.get(portletPreferencesHBM.getPrimaryKey());
        if (portletPreferences == null) {
            portletPreferences = new com.liferay.portal.model.PortletPreferences(portletPreferencesHBM.getPortletId(), portletPreferencesHBM.getLayoutId(), portletPreferencesHBM.getUserId(), portletPreferencesHBM.getPreferences());
            PortletPreferencesPool.put(portletPreferences.getPrimaryKey(), portletPreferences);
        }
        return portletPreferences;
    }
}
