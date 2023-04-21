    private void storeAttachments(GenericValue task, GenericValue report) {
        _log.info("Processing attachments");
        ProjectDAO projectDAO = new ProjectDAO();
        List<GenericValue> attachments = projectDAO.getProjectTaskAttach(task);
        for (GenericValue attachment : attachments) {
            _log.info("Saving attachment " + attachment.getString("file"));
            String aSource = _loader.getProperty(attachment.getString("file"), true);
            String aDest = _loader.getProperty(Constants.SYSTEM_CONFIG_ATTACH_PATH, false);
            _log.info("Source " + aSource);
            _log.info("Destination " + aDest);
            File fs = new File(aSource);
            if (fs.getParent() == null) {
                fs = new File(getDirectory() + Constants.FILE_SEP_FW + aSource);
            }
            if (fs.exists()) {
                GenericValue e = ReportDAO.getNewReportAttachment();
                String destReportDir = task.getString("project") + Constants.FILE_SEP_FW + report.getString("projectReport") + Constants.FILE_SEP_FW + e.getString("id");
                aDest += Constants.FILE_SEP_FW + destReportDir;
                File fd = new File(aDest);
                if (!fd.exists()) fd.mkdirs();
                try {
                    if (fs.isFile()) {
                        FileUtils.copyFileToDirectory(fs, fd);
                    } else {
                        FileUtils.copyDirectory(fs, fd);
                    }
                } catch (IOException ex) {
                    _log.error("Unable to save attachment", ex);
                }
                e.set("log", report.get("id"));
                e.set("file", destReportDir);
                e.set("url", destReportDir);
                e.set("description", attachment.get("description"));
                _attachments.add(e);
            } else {
                _log.warn("File attachment [" + fs.getAbsolutePath() + "] does not exist");
            }
        }
    }
