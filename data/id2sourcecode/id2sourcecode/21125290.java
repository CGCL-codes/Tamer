    @Override
    public void command_factory() {
        command_list.addElement(new RemoveAllDynAttrClass("RemoveAllDynAttr", Tango_DEV_VOID, Tango_DEV_VOID, "", "", DispLevel.EXPERT));
        command_list.addElement(new RemoveDynAttrsClass("RemoveDynAttrs", Tango_DEVVAR_STRINGARRAY, Tango_DEV_VOID, "", "", DispLevel.OPERATOR));
        command_list.addElement(new GetSnapClass("GetSnap", Tango_DEV_LONG, Tango_DEVVAR_STRINGARRAY, "snapID", "[attrRealName, dynAttrNameW,dynAttrNameR]*n", DispLevel.OPERATOR));
        command_list.addElement(new GetSnapValueClass("GetSnapValue", Tango_DEVVAR_STRINGARRAY, Tango_DEVVAR_STRINGARRAY, "snapID and attribute name", "Attribute's Read value and Write value", DispLevel.OPERATOR));
        command_list.addElement(new RemoveDynAttrClass("RemoveDynAttr", Tango_DEV_STRING, Tango_DEV_VOID, "", "", DispLevel.OPERATOR));
        command_list.addElement(new GetSnapsForContextClass("GetSnapsForContext", Tango_DEV_LONG, Tango_DEVVAR_LONGSTRINGARRAY, "", "", DispLevel.OPERATOR));
        command_list.addElement(new GetSnapIDClass("GetSnapID", Tango_DEVVAR_STRINGARRAY, Tango_DEVVAR_LONGARRAY, "ctx_id, criterion: \nSyntax: ctx_id, \"id_snap > | < | = | <= | >= nbr\",\n" + " \"time < | > | >= | <=  yyyy-mm-dd hh:mm:ss | dd-mm-yyyy hh:mm:ss\"," + "\n \"comment starts | ends | contains string\",\n first | last", "list of snapshot_id", DispLevel.OPERATOR));
        command_list.addElement(new GetSnapValuesClass("GetSnapValues", Tango_DEVVAR_STRINGARRAY, Tango_DEVVAR_STRINGARRAY, "snapID, true for read values or false for write values ,attribute names", "Attribute's Read value and Write value", DispLevel.OPERATOR));
        for (int i = 0; i < command_list.size(); i++) {
            Command cmd = (Command) command_list.elementAt(i);
        }
    }
