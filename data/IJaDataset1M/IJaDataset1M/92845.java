package com.videoshare.user.dao;

/**
 *
 * @author  Administrator
 */
public class TableFieldHandler {

    String fieldType = "";

    Object fieldObject = null;

    String columnName = "";

    /** Creates a new instance of FieldHandler */
    public TableFieldHandler(String fieldType, Object fieldObject, String columnName) {
        this.fieldType = fieldType;
        this.fieldObject = fieldObject;
        this.columnName = columnName;
    }

    public String getType() {
        return fieldType;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getObject() {
        return fieldObject;
    }
}
