    private boolean processProjectNumber(File file, String project) {
        if ((file == null) || (project == null)) {
            return false;
        }
        File topologyFile = null;
        File trajectoryFile = null;
        File logFile = null;
        boolean pourcentEnough = false;
        boolean specialInterval = false;
        if (file.isFile() && "current.xyz".equalsIgnoreCase(file.getName())) {
            for (int i = 0; (i < 10) && !pourcentEnough; i++) {
                Object[] objects = new Object[] { Integer.valueOf(i) };
                File infoFile = new File(file.getParentFile(), String.format("wuinfo_%1$02d.dat", objects));
                if (infoFile.exists()) {
                    topologyFile = new File(file.getParentFile(), String.format("wudata_%1$02d.top", objects));
                    trajectoryFile = new File(file.getParentFile(), String.format("wudata_%1$02d.trj", objects));
                    logFile = new File(file.getParentFile(), String.format("logfile_%1$02d.txt", objects));
                    if (topologyFile.exists() && trajectoryFile.exists() && logFile.exists()) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(logFile));
                            try {
                                String line = null;
                                while (!pourcentEnough && (line = reader.readLine()) != null) {
                                    if (line != null) {
                                        for (int percent = configuration.getThreshold(); percent <= 100; percent++) {
                                            if (line.contains(Integer.toString(percent) + "%")) {
                                                specialInterval = true;
                                            }
                                        }
                                        if (line.contains("98%") || line.contains("99%")) {
                                            pourcentEnough = true;
                                        }
                                    }
                                }
                            } catch (IOException e) {
                            } finally {
                                if (reader != null) {
                                    try {
                                        reader.close();
                                    } catch (IOException e) {
                                    }
                                }
                            }
                            if (!pourcentEnough) {
                                topologyFile = null;
                                trajectoryFile = null;
                                logFile = null;
                            }
                        } catch (FileNotFoundException e) {
                        }
                    } else {
                        topologyFile = null;
                        trajectoryFile = null;
                        logFile = null;
                    }
                }
            }
        }
        if ((!availableProjects.exists()) || (!availableAmbers.exists())) {
            downloadAvailableFiles();
        }
        updateExistingProjects();
        if (existingProjects.contains(project) && (existingAmbers.contains(project) || (topologyFile == null) || (trajectoryFile == null))) {
            if (configuration.getDetailedOutput()) {
                System.out.print("Project available on Jmol website");
            }
            return (specialInterval && !existingAmbers.contains(project) && !configuration.hasBeenSent("A_" + project));
        }
        if (configuration.hasBeenSent(project) && ((topologyFile == null) || (trajectoryFile == null) || configuration.hasBeenSent("A_" + project))) {
            if (configuration.getDetailedOutput()) {
                System.out.print("Already sent by you");
            }
            return false;
        }
        if ((!availableProjectsDownloaded) || (!availableAmbersDownloaded)) {
            downloadAvailableFiles();
            updateExistingProjects();
            if (existingProjects.contains(project) && (existingAmbers.contains(project) || (topologyFile == null) || (trajectoryFile == null))) {
                if (configuration.getDetailedOutput()) {
                    System.out.print("Project available on Jmol website");
                }
                return (specialInterval && !existingAmbers.contains(project));
            }
        }
        try {
            System.out.println("Found new project :) " + project);
            File[] files = new File[4];
            files[0] = file;
            if ((topologyFile != null) && (trajectoryFile != null)) {
                files[1] = topologyFile;
                files[2] = trajectoryFile;
                if (logFile != null) {
                    files[3] = logFile;
                }
            }
            if ((configuration.getMailServer() != null) && (!"".equals(configuration.getMailServer())) && (configuration.getUserMail() != null) && (!"".equals(configuration.getUserMail())) && (configuration.getUserName() != null) && (!"".equals(configuration.getUserName()))) {
                MailSender sender = new MailSender(configuration, project, files, false);
                sender.sendMail();
            }
            if ((configuration.getSaveDirectory() != null) && (!"".equals(configuration.getSaveDirectory()))) {
                File saveDir = new File(configuration.getSaveDirectory());
                try {
                    FileUtils.forceMkdir(saveDir);
                } catch (IOException e) {
                    System.out.println("Error creating directory " + saveDir.getAbsolutePath());
                }
                for (int i = 0; i < files.length; i++) {
                    File saveProjectDir = new File(saveDir, "p" + project);
                    if (files[i] != null) {
                        File destFile = new File(saveProjectDir, files[i].getName());
                        try {
                            FileUtils.copyFile(files[i], destFile);
                        } catch (IOException e) {
                            System.out.println("Error saving file " + files[i]);
                        }
                    }
                }
            }
            if (!existingProjects.contains(project)) {
                if (!configuration.hasBeenSent(project)) {
                    configuration.addSentFile(project);
                }
                sentProjects.add(project);
            }
            if (!existingAmbers.contains(project) && (topologyFile != null) && (trajectoryFile != null)) {
                if (!configuration.hasBeenSent("A_" + project)) {
                    configuration.addSentFile("A_" + project);
                }
                sentAmbers.add(project);
            }
        } catch (Throwable e) {
            outputError("Sending new file", e);
        }
        return false;
    }
