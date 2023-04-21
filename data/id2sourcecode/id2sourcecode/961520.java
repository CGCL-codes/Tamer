    @Override
    public void run() {
        try {
            if (defaultServer == null) defaultServer = ServerTools.loadDefaultServer();
            while (!isInterrupted()) {
                ClientJob job = Jadif.getWork(defaultServer);
                int jobid = job.getId();
                String uniqueId = job.getServer().getUniqueId();
                Connection con = DatabaseConnection.openDB("excrawler");
                String sql = "SELECT * FROM ex_crawllist WHERE jobId = ? AND status = 0";
                PreparedStatement statement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                statement.setInt(1, job.getId());
                ResultSet entry = statement.executeQuery();
                entry.last();
                int rows = entry.getRow();
                entry.beforeFirst();
                if (rows == 0) {
                    entry.close();
                    statement.close();
                    con.close();
                    String workcont = job.getWork().trim();
                    String[] links = workcont.split(",");
                    for (int i = 0; i < links.length; i++) {
                        WorkerHelper.saveNewLink(uniqueId, jobid, links[i].trim());
                    }
                    con = DatabaseConnection.openDB("excrawler");
                    statement = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    statement.setInt(1, job.getId());
                    entry = statement.executeQuery();
                }
                while (entry.next()) {
                    int returncode = 0;
                    int id = entry.getInt("id");
                    String url = entry.getString("url");
                    ThreadControl.WorkerStatus = "Downloading url: " + url;
                    String filename = WorkerHelper.downloadURL(id, jobid, url);
                    if (url.endsWith(".pdf")) {
                        returncode = 2;
                    } else if (url.endsWith(".doc") || url.endsWith(".docx")) {
                        returncode = 2;
                    } else {
                        returncode = 2;
                    }
                    if (returncode == 0) {
                        entry.updateInt("status", 1);
                        entry.updateRow();
                    } else if (returncode == 2) {
                        entry.updateInt("status", 2);
                        entry.updateRow();
                    } else if (returncode == -1) {
                    } else {
                        entry.updateInt("status", 7);
                        entry.updateRow();
                    }
                }
                entry.close();
                statement.close();
                con.close();
                Jadif.sendWork(job);
            }
        } catch (Exception e) {
            Log.logger.fatal("Error in worker", e);
            new Worker(defaultServer).start();
            return;
        }
    }
