    private void execute() throws Exception {
        isRunning = true;
        long start = TimeUtil.getTrancheTimestamp();
        final String buttonLabel = this.executeButton.getText();
        try {
            this.executeButton.setEnabled(false);
            this.executeButton.setText("Running...");
            System.out.println("Executing project replication tool at " + TextUtil.getFormattedDate(start) + ":");
            int replications = -1;
            try {
                replications = Integer.parseInt(this.replicationsTextField.getText());
            } catch (NumberFormatException nfe) {
            }
            if (replications < 1) {
                GenericOptionPane.showMessageDialog(frame, "You must offer a desired minimum number of replications above zero. (If you are unsure, type the number '3'.)\n\nThe following input is not a positive number: " + this.replicationsTextField.getText(), "Must specify number of replications", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            System.out.println(" * " + replications + " replications");
            List<String> serversToRead = new ArrayList();
            serversToRead.addAll(this.serversToReadPanel.getSelectedHosts());
            boolean noServersToRead = false;
            if (serversToRead.size() == 0) {
                noServersToRead = true;
            }
            System.out.println(" * " + serversToRead.size() + " servers for reading:");
            for (String url : serversToRead) {
                System.out.println("   - " + url);
            }
            List<String> serversToWrite = new ArrayList();
            serversToWrite.addAll(this.serversToWritePanel.getSelectedHosts());
            System.out.println(" * " + serversToWrite.size() + " servers for writing:");
            for (String url : serversToWrite) {
                System.out.println("   - " + url);
            }
            boolean noServersToWrite = false;
            if (serversToWrite.size() == 0) {
                noServersToWrite = true;
            }
            if (noServersToRead || noServersToWrite) {
                String readServers = String.valueOf(serversToRead.size()) + " ";
                readServers += (serversToRead.size() == 1 ? "server" : "servers");
                String writeServers = String.valueOf(serversToWrite.size()) + " ";
                writeServers += (serversToWrite.size() == 1 ? "server" : "servers");
                GenericOptionPane.showMessageDialog(frame, "There are " + readServers + " to read and " + writeServers + " to write.\n\nMust be at least one server in each pool.\n\nCheck the servers menu and select more servers.\n\nNote that this may happen right after logging in. Sometimes simply viewing the two servers lists solves the problem.", "Check server pools", JOptionPane.ERROR_MESSAGE);
                return;
            }
            UserZipFile uzf = this.userButton.getUser();
            System.out.println(" * " + uzf.getUserNameFromCert());
            List<ProjectsEntry> projects = this.projectsTableModel.getProjects();
            System.out.println(" * " + projects.size() + " projects");
            int currentProjectCount = 1;
            final int totalProjectCount = projects.size();
            for (ProjectsEntry project : projects) {
                progressBar.reset();
                progressBar.setPrefixString("Project " + String.valueOf(currentProjectCount) + " of " + String.valueOf(totalProjectCount));
                System.out.println("   - Starting next project: " + project.getHash());
                ProjectReplicationTool replicationTool = new ProjectReplicationTool(uzf.getCertificate(), uzf.getPrivateKey(), serversToRead, serversToWrite);
                replicationTool.setHash(project.getHash());
                if (project.getPassphrase() != null && project.getPassphrase().trim().equals("")) {
                    replicationTool.setPassphrase(project.getPassphrase());
                }
                replicationTool.setNumberRequiredReplications(replications);
                replicationTool.addProjectReplicationToolListener(progressBar);
                replicationTool.execute();
                currentProjectCount++;
            }
        } finally {
            isRunning = false;
            this.executeButton.setText(buttonLabel);
            executableListener.checkIfExecutable();
        }
        GenericOptionPane.showMessageDialog(frame, "Tool ran for " + TextUtil.formatTimeLength(TimeUtil.getTrancheTimestamp() - start), "Tool finished at " + TextUtil.getFormattedDate(TimeUtil.getTrancheTimestamp()), JOptionPane.INFORMATION_MESSAGE);
    }
