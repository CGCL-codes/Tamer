package org.wfp.rita.web.mbeans;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.faces.model.SelectItem;
import org.wfp.rita.base.RitaException;
import org.wfp.rita.dao.Location;
import org.wfp.rita.dao.Site;
import org.wfp.rita.datafacade.DataFacade;
import org.wfp.rita.web.common.MBeanUtils;

/**
 * Get a list of matching locations for a specific site. Use this class as the
 * backing bean to populate list of locations when location is specified
 * together with a selection of a site
 * 
 * @author rkiraly@wfp.org
 * 
 */
public class LocationListBean {

    private String siteId;

    /**
	 * Dispatch type can be 
	 */
    private String dispatchType;

    public void setSiteId(String siteId) throws RitaException {
        this.siteId = siteId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getDispatchType() {
        return dispatchType;
    }

    public void setDispatchType(String dispatchType) {
        this.dispatchType = dispatchType;
    }

    public List<SelectItem> getlocationList() throws RitaException {
        List<SelectItem> retList = new LinkedList<SelectItem>();
        DataFacade f = MBeanUtils.getDataFacade();
        Site destSite = f.getSiteDao().getSite(Integer.valueOf(siteId));
        Set<Location> locations = destSite.getChildLocations();
        for (Location loc : locations) {
            if (dispatchType != null) {
                if (dispatchType.contains(loc.getLocationType().getCode().name())) {
                    retList.add(new SelectItem(loc.getId().getCompositeKeyHash(), loc.getName()));
                }
            } else {
                retList.add(new SelectItem(loc.getId().getCompositeKeyHash(), loc.getName()));
            }
        }
        return retList;
    }
}
