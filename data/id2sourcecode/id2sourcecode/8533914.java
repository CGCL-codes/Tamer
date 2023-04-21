    public static void shutdown() {
        String backupdir = Prefs.getPref(PrefName.BACKUPDIR);
        if (backupdir != null && !backupdir.equals("")) {
            try {
                int ret = JOptionPane.showConfirmDialog(null, Resource.getResourceString("backup_notice") + " " + backupdir + "?", "BORG", JOptionPane.OK_CANCEL_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String uniq = sdf.format(new Date());
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(backupdir + "/borg" + uniq + ".zip"));
                    Writer fw = new OutputStreamWriter(out, "UTF8");
                    out.putNextEntry(new ZipEntry("borg.xml"));
                    AppointmentModel.getReference().export(fw);
                    fw.flush();
                    out.closeEntry();
                    out.putNextEntry(new ZipEntry("task.xml"));
                    TaskModel.getReference().export(fw);
                    fw.flush();
                    out.closeEntry();
                    out.putNextEntry(new ZipEntry("addr.xml"));
                    AddressModel.getReference().export(fw);
                    fw.flush();
                    out.closeEntry();
                    if (MemoModel.getReference().hasMemos()) {
                        out.putNextEntry(new ZipEntry("memo.xml"));
                        MemoModel.getReference().export(fw);
                        fw.flush();
                        out.closeEntry();
                    }
                    if (LinkModel.getReference().hasLinks()) {
                        out.putNextEntry(new ZipEntry("link.xml"));
                        LinkModel.getReference().export(fw);
                        fw.flush();
                        out.closeEntry();
                    }
                    out.close();
                }
            } catch (Exception e) {
                Errmsg.errmsg(e);
            }
        }
        try {
            Banner ban = new Banner();
            ban.setText(Resource.getPlainResourceString("shutdown"));
            ban.setVisible(true);
            AppointmentModel.getReference().getDB().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer shutdownTimer = new java.util.Timer();
        shutdownTimer.schedule(new TimerTask() {

            public void run() {
                System.exit(0);
            }
        }, 3 * 1000, 28 * 60 * 1000);
    }
