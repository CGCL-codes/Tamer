    @Override
    protected void workOut(int recNum) {
        Recording rec = getProcessDefinition().getSubjects().get(recNum);
        String format = "%03d.vdr";
        File targetDir = getCutPath(rec, getJob().getTarget());
        File target = null;
        int i = 0;
        if ("TS".compareTo(getJob().getTarget()) == 0) format = "%05d.ts";
        for (File source : inputFiles) {
            target = new File(targetDir, String.format(format, ++i));
            log("should move file ]" + source + "[ => ]" + target + "[");
            FileUtils.moveOrRename(target, source);
        }
        for (File source : rec.getPath().listFiles()) {
            if ("info.vdr".compareTo(source.getName()) == 0 || "info".compareTo(source.getName()) == 0) {
                if ("TS".compareTo(getJob().getTarget()) == 0) target = new File(targetDir, "info"); else target = new File(targetDir, "info.vdr");
                log("should copy file ]" + source + "[ => ]" + target + "[");
                FileUtils.copyFile(target, source);
            } else if (source.getName().startsWith("info.")) {
                target = new File(targetDir, source.getName());
                log("should copy file ]" + source + "[ => ]" + target + "[");
                FileUtils.copyFile(target, source);
            }
        }
    }
