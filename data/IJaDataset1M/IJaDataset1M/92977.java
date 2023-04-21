package com.techstar.dmis.dto;

import java.io.Serializable;
import com.techstar.framework.service.dto.DictionaryBaseDto;

/**
 * Domain classe for 调度机构
 * This classe is based on ValueObject Pattern
 * @author 
 * @date
 */
public class StdDispatchunitDto extends DictionaryBaseDto implements Serializable {

    public StdDispatchunitDto() {
    }

    private String unitname;

    private String unitarea;

    private String unitshortname;

    private String scheduleunit;

    private int displayno;

    private String iscanedit;

    private String maintenanceunitid;

    private int version;

    /**
     * getters and setters
     */
    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitarea(String unitarea) {
        this.unitarea = unitarea;
    }

    public String getUnitarea() {
        return unitarea;
    }

    public void setUnitshortname(String unitshortname) {
        this.unitshortname = unitshortname;
    }

    public String getUnitshortname() {
        return unitshortname;
    }

    public void setScheduleunit(String scheduleunit) {
        this.scheduleunit = scheduleunit;
    }

    public String getScheduleunit() {
        return scheduleunit;
    }

    public void setDisplayno(int displayno) {
        this.displayno = displayno;
    }

    public int getDisplayno() {
        return displayno;
    }

    public void setIscanedit(String iscanedit) {
        this.iscanedit = iscanedit;
    }

    public String getIscanedit() {
        return iscanedit;
    }

    public void setMaintenanceunitid(String maintenanceunitid) {
        this.maintenanceunitid = maintenanceunitid;
    }

    public String getMaintenanceunitid() {
        return maintenanceunitid;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
