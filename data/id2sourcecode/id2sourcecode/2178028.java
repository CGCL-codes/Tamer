    private void createLogMgtPage(body bb) {
        bb.addElement(new hr());
        table table = new table();
        table.setBorder(1);
        table.setCellSpacing(2);
        table.setCellPadding(2);
        Properties ctx = new Properties();
        MSystem system = MSystem.get(ctx);
        tr line = new tr();
        line.addElement(new th().addElement(system.getDBAddress()));
        line.addElement(new td().addElement(Ini.getOXPHome()));
        table.addElement(line);
        line = new tr();
        String info = System.getProperty("os.name") + " " + System.getProperty("os.version");
        String s = System.getProperty("sun.os.patch.level");
        if ((s != null) && (s.length() > 0)) {
            info += " (" + s + ")";
        }
        line.addElement(new th().addElement(info));
        info = system.getName();
        if (system.getCustomPrefix() != null) {
            info += " (" + system.getCustomPrefix() + ")";
        }
        line.addElement(new td().addElement(info));
        table.addElement(line);
        line = new tr();
        info = System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version");
        line.addElement(new th().addElement(info));
        line.addElement(new td().addElement(system.getUserName()));
        table.addElement(line);
        line = new tr();
        BaseDatosOXP db = CConnection.get().getDatabase();
        info = db.getDescription();
        line.addElement(new th().addElement(info));
        line.addElement(new td().addElement(system.getDBInstance()));
        table.addElement(line);
        line = new tr();
        line.addElement(new th().addElement("Processor/Support"));
        line.addElement(new td().addElement(system.getNoProcessors() + "/" + system.getSupportUnits()));
        table.addElement(line);
        line = new tr();
        line.addElement(new th().addElement(new label("TraceLevel").addElement("Trace Log Level")));
        form myForm = new form("MonitorOXP", form.METHOD_POST, form.ENC_DEFAULT);
        option[] options = new option[CLogMgt.LEVELS.length];
        for (int i = 0; i < options.length; i++) {
            options[i] = new option(CLogMgt.LEVELS[i].getName());
            options[i].addElement(CLogMgt.LEVELS[i].getName());
            if (CLogMgt.LEVELS[i] == CLogMgt.getLevel()) {
                options[i].setSelected(true);
            }
        }
        select sel = new select("TraceLevel", options);
        myForm.addElement(sel);
        myForm.addElement(new input(input.TYPE_SUBMIT, "Set", "Set"));
        line.addElement(new td().addElement(myForm));
        table.addElement(line);
        line = new tr();
        CLogFile fileHandler = CLogFile.get(true, null);
        line.addElement(new th().addElement("Trace File"));
        line.addElement(new td().addElement(new a("MonitorOXP?Trace=" + fileHandler.getFileName(), "Current")));
        table.addElement(line);
        line = new tr();
        line.addElement(new td().addElement(new a("MonitorOXP?Trace=ROTATE", "Rotate Trace Log")));
        line.addElement(new td().addElement(new a("MonitorOXP?Trace=DELETE", "Delete all Trace Logs")));
        table.addElement(line);
        bb.addElement(table);
        p p = new p();
        p.addElement(new b("All Log Files: "));
        File logDir = fileHandler.getLogDirectory();
        if ((logDir != null) && logDir.isDirectory()) {
            File[] logs = logDir.listFiles();
            for (int i = 0; i < logs.length; i++) {
                if (i != 0) {
                    p.addElement(" - ");
                }
                String fileName = logs[i].getAbsolutePath();
                a link = new a("MonitorOXP?Trace=" + fileName, fileName);
                p.addElement(link);
                int size = (int) (logs[i].length() / 1024);
                if (size < 1024) {
                    p.addElement(" (" + size + "k)");
                } else {
                    p.addElement(" (" + size / 1024 + "M)");
                }
            }
        }
        bb.addElement(p);
    }
