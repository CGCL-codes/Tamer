    private void runTask(TaskCommand taskCommand, boolean waitForExit, int deviceID, String deviceString, File capFile) {
        String fileName = "";
        if (capFile != null) fileName = capFile.getName();
        item.log("Running task (" + taskCommand.getName() + ") on file (" + fileName + ")");
        System.out.println(this + " : Running task (" + taskCommand.getName() + ") on file (" + fileName + ")");
        if (taskCommand.getEnabled() == false) {
            item.log("Task (" + taskCommand.getName() + ") is disabled.");
            System.out.println(this + " : Task (" + taskCommand.getName() + ") is disabled.");
            return;
        }
        String command = taskCommand.getCommand();
        if (command != null && command.length() > 1) {
            int index = command.indexOf("$deviceID");
            if (index > -1) {
                StringBuffer buff = new StringBuffer(command);
                buff.replace(index, index + "$deviceID".length(), new Integer(deviceID).toString());
                command = buff.toString();
            }
            command.replaceAll("$deviceString", deviceString);
            index = command.indexOf("$deviceString");
            if (index > -1) {
                StringBuffer buff = new StringBuffer(command);
                buff.replace(index, index + "$deviceString".length(), deviceString);
                command = buff.toString();
            }
            index = command.indexOf("$filename");
            if (index > -1 && capFile != null) {
                String fullPath = capFile.getPath();
                try {
                    fullPath = capFile.getCanonicalPath();
                } catch (Exception e) {
                }
                StringBuffer buff = new StringBuffer(command);
                buff.replace(index, index + "$filename".length(), fullPath);
                command = buff.toString();
            }
            index = command.indexOf("$capType");
            if (index > -1) {
                int captureType = item.getCapType();
                if (captureType == -1) {
                    Channel ch = store.getChannel(item.getChannel());
                    if (ch != null) captureType = ch.getCaptureType();
                }
                if (captureType == -1) {
                    try {
                        captureType = Integer.parseInt(store.getProperty("Capture.deftype"));
                    } catch (Exception e) {
                    }
                }
                String capType = new Integer(captureType).toString();
                StringBuffer buff = new StringBuffer(command);
                buff.replace(index, index + "$capType".length(), capType);
                command = buff.toString();
            }
            try {
                runCommand(taskCommand, command, item, waitForExit, capFile);
            } catch (Exception e) {
                item.log("There was an Exception thrown when trying to run the task.");
                e.printStackTrace();
            }
        }
    }
