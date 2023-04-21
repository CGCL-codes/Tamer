    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        super.execute(context);
        debug("Start execute job " + this.getClass().getName());
        String file = conf.getDb().getConn().replaceAll("jdbc:sqlite:", "");
        String file2 = conf.getStats().getConn().replaceAll("jdbc:sqlite:", "");
        String ts = DateTimeUtil.getNowWithFormat("yyyyMMdd");
        if (properties.get("dir") == null) {
            fatal("Properties Dir not set");
            return;
        }
        String dir = properties.get("dir") + ts + "/";
        debug("dir to write " + dir);
        try {
            File db = new File(file);
            File stats = new File(file2);
            org.apache.commons.io.FileUtils.forceMkdir(new File(dir));
            debug("force dir:" + dir);
            org.apache.commons.io.FileUtils.copyFileToDirectory(db, new File(dir));
            debug("copy from:" + file + " to " + dir);
            org.apache.commons.io.FileUtils.copyFileToDirectory(stats, new File(dir));
            debug("copy from:" + file2 + " to " + dir);
            File db_backup = new File(dir + "/" + db.getName());
            File db_backup_to = new File(dir + "/" + db.getName() + ".zip");
            File stats_backup = new File(dir + "/" + stats.getName());
            File stats_backup_to = new File(dir + "/" + stats.getName() + ".zip");
            gzip(db_backup, db_backup_to);
            gzip(stats_backup, stats_backup_to);
            boolean r = db_backup.delete();
            debug("remove file " + db_backup + " : " + r);
            r = stats_backup.delete();
            debug("remove file " + stats_backup + " : " + r);
        } catch (IOException ex) {
            fatal("IOException", ex);
        }
        debug("End execute job " + this.getClass().getName());
        com.conf.Mail mail = this.conf.getMail();
        if (mail.isEnable()) {
            debug("Start send Mail");
            boolean r = MailUtil.sendMail(mail.getHostname(), mail.getPort(), mail.getUsername(), mail.getPassword(), "marco@bluestudio.it", "marcoberri@gmail.com", "test di backup", "test di backup completo", null);
            if (!r) {
                fatal("Errore nel spedire la mail:" + r);
            }
        }
    }
