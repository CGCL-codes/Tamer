package pl.xperios.rdk.shared.beans;

import java.util.ArrayList;
import java.util.HashMap;
import pl.xperios.rdk.shared.TreeBean;

public class Role extends TreeBean<Long> {

    public Role() {
    }

    public Role(Long roleId, String code, String fullName, String description, Long entityId, Long parentRoleId, Boolean visible, Boolean active, java.util.Date startDt, java.util.Date stopDt) {
        set("role_id", roleId);
        set("code", code);
        set("full_name", fullName);
        set("description", description);
        set("entity_id", entityId);
        set("parent_role_id", parentRoleId);
        set("visible", visible);
        set("active", active);
        set("start_dt", startDt);
        set("stop_dt", stopDt);
    }

    public Long getRoleId() {
        return get("role_id");
    }

    public void setRoleId(Long role_id) {
        set("role_id", role_id);
    }

    public String getCode() {
        return get("code");
    }

    public void setCode(String code) {
        set("code", code);
    }

    public String getFullName() {
        return get("full_name");
    }

    public void setFullName(String full_name) {
        set("full_name", full_name);
    }

    public String getDescription() {
        return get("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public Long getEntityId() {
        return get("entity_id");
    }

    public void setEntityId(Long entity_id) {
        set("entity_id", entity_id);
    }

    public Long getParentRoleId() {
        return get("parent_role_id");
    }

    public void setParentRoleId(Long parent_role_id) {
        set("parent_role_id", parent_role_id);
    }

    public Boolean getVisible() {
        return get("visible");
    }

    public void setVisible(Boolean visible) {
        set("visible", visible);
    }

    public Boolean getActive() {
        return get("active");
    }

    public void setActive(Boolean active) {
        set("active", active);
    }

    public java.util.Date getStartDt() {
        return get("start_dt");
    }

    public void setStartDt(java.util.Date start_dt) {
        set("start_dt", start_dt);
    }

    public java.util.Date getStopDt() {
        return get("stop_dt");
    }

    public void setStopDt(java.util.Date stop_dt) {
        set("stop_dt", stop_dt);
    }

    @Override
    public String getPropertyId() {
        return "role_id";
    }

    @Override
    public ArrayList<String> getAllPropertiesNames() {
        ArrayList<String> out = new ArrayList<String>();
        out.add("role_id");
        out.add("code");
        out.add("full_name");
        out.add("description");
        out.add("entity_id");
        out.add("parent_role_id");
        out.add("visible");
        out.add("active");
        out.add("start_dt");
        out.add("stop_dt");
        return out;
    }

    @Override
    protected HashMap<String, String> initPropertiesClass() {
        HashMap<String, String> out = new HashMap<String, String>();
        out.put("role_id", Long.class.getName());
        out.put("code", String.class.getName());
        out.put("full_name", String.class.getName());
        out.put("description", String.class.getName());
        out.put("entity_id", Long.class.getName());
        out.put("parent_role_id", Long.class.getName());
        out.put("visible", Boolean.class.getName());
        out.put("active", Boolean.class.getName());
        out.put("start_dt", java.util.Date.class.getName());
        out.put("stop_dt", java.util.Date.class.getName());
        return out;
    }

    @Override
    public String toString() {
        return "Role{" + "role_id:" + get("role_id") + "," + "code:" + get("code") + "," + "full_name:" + get("full_name") + "," + "description:" + get("description") + "," + "entity_id:" + get("entity_id") + "," + "parent_role_id:" + get("parent_role_id") + "," + "visible:" + get("visible") + "," + "active:" + get("active") + "," + "start_dt:" + get("start_dt") + "," + "stop_dt:" + get("stop_dt") + "," + "}";
    }
}
