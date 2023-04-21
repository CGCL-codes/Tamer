    public void GenerateDescFiles(SZGWorkflow templatewf, String templatejobname, String userId, String workflowname, String jobname) {
        String path = PropertyLoader.getPrefixDir() + "/users/" + templatewf.getUserId().toString() + "/" + templatewf.getId().toString() + "_files/";
        String destpath = PropertyLoader.getPrefixDir() + "/users/" + userId + "/" + workflowname + "_files/";
        MiscUtils.writeStrToFile(destpath + jobname + ".grid", MiscUtils.readFileToStr(path + templatejobname + ".grid"));
        MiscUtils.writeStrToFile(destpath + jobname + ".owner", userId);
        String templatejobdesc = WorkflowUtils.loadJobDesc(templatewf.getUserId().toString(), templatewf.getId().toString(), templatejobname);
        String grid = templatejobdesc.split(" ")[3];
        String host = templatejobdesc.split(" ")[4];
        String monitor = templatejobdesc.split(" ")[5];
        String type = templatejobdesc.split(" ")[6];
        String desc = userId + " " + workflowname + " " + jobname + " " + grid + " " + host + " " + monitor + " " + type;
        MiscUtils.writeStrToFile(destpath + "." + jobname + ".desc", desc);
    }
