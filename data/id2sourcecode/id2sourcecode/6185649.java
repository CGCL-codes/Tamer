    @Override
    public void command_factory() {
        command_list.addElement(new CreateNewContextClass("CreateNewContext", Tango_DEVVAR_STRINGARRAY, Tango_DEV_LONG, "All the informations usefull to create a context ,Snapshot pattern).", "The new assigned context ID", DispLevel.OPERATOR));
        command_list.addElement(new SetEquipmentsWithSnapshotClass("SetEquipmentsWithSnapshot", Tango_DEVVAR_STRINGARRAY, Tango_DEV_VOID, "The snapshot from which equipments are set.", "", DispLevel.OPERATOR));
        command_list.addElement(new SetEquipmentsWithCommandClass("SetEquipmentsWithCommand", Tango_DEVVAR_STRINGARRAY, Tango_DEV_STRING, "The command name,  STORED_READ_VALUE || STORED_WRITE_VALUE ,\n and the snapshot ID from which equipments are set.", "", DispLevel.OPERATOR));
        command_list.addElement(new SetEquipmentsClass("SetEquipments", Tango_DEVVAR_STRINGARRAY, Tango_DEV_VOID, "* First Case: Setpoint is  done on all the snapshot attributes:\n" + "  - argin[0]= the snap identifier\n" + "  - argin[1]=STORED_READ_VALUE (Setpoint with theirs read values) or STORED_WRITE_VALUE (Setpoint with theirs write values)\n\n" + "* Second Case: Setpoint is done on a set of the snapshot attributes:\n " + "  - argin[0]= the snap identifier\n" + "  - argin[1]=the number of attributes.\n" + " Let us note index the last index used (for example, at this point,index = 2).\n" + "  - argin[index]=NEW_VALUE or STORED_READ_VALUE or STORED_WRITE_VALUE\n" + "  - argin[index+1]= the attribut name\n" + "  - argin[index+2]= the value to set when NEW_VALUE is requested", "", DispLevel.OPERATOR));
        command_list.addElement(new UpdateSnapCommentClass("UpdateSnapComment", Tango_DEVVAR_LONGSTRINGARRAY, Tango_DEV_VOID, "1) snapshot identifier 2) The new comment", "", DispLevel.OPERATOR));
        command_list.addElement(new LaunchSnapShotCmd("LaunchSnapShot", Tango_DEV_SHORT, Tango_DEV_VOID, "The snapshot associated context's identifier", "", DispLevel.OPERATOR));
        command_list.addElement(new TemplCommandInOut("GetSnapShotResult", "getSnapShotResult", "The snapshot associated context's identifier", "The new snapshot identifier", DispLevel.OPERATOR));
        command_list.addElement(new TemplCommandInOut("GetSnapShotComment", "getSnapComment", "The snapshot id", "The comment", DispLevel.OPERATOR));
        for (int i = 0; i < command_list.size(); i++) {
            final Command cmd = (Command) command_list.elementAt(i);
        }
    }
