    private void BuildPackage(String params[]) {
        StatusArea.setText("");
        StatusArea.setText(StatusArea.getText() + "Starting Build...\n");
        ProgressBar.setValue(2);
        StatusArea.setText(StatusArea.getText() + "Preparing to write Configuration file...\n");
        ProgressBar.setValue(4);
        StatusArea.setText(StatusArea.getText() + "Reading parameter values...\nList of values:\n\n");
        int i = 0;
        while (i != 13) {
            StatusArea.setText(StatusArea.getText() + "Value " + String.valueOf(i) + " = " + params[i] + "\n");
            i++;
        }
        ProgressBar.setValue(6);
        params[6] = params[6].trim();
        String strTmp = params[6];
        if (!strTmp.startsWith("/")) {
            params[6] = "/" + strTmp;
        }
        try {
            String directory = params[6] + "/DEBIAN";
            boolean success = (new File(directory)).mkdir();
            ProgressBar.setValue(8);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Packin has encountered an exception while creating the configuration directory.\nPackin will try to bypass this exception and continue building the package.\n\nNOTE: This exception was likely caused by ineffecient read/write permissions with the \nspecified root folder. If this is the case, modify permissions and rerun Packin.");
        }
        try {
            boolean exists = (new File(params[6] + "/DEBIAN/control")).exists();
            if (exists) {
                boolean success2 = (new File(params[6] + "/DEBIAN/control")).delete();
            }
            File configFile = new File(params[6] + "DEBIAN/config");
            configFile.createNewFile();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Packin has encountered an exception while creating the configuration files(s).\nPackin will try to bypass this exception and continue building the package.\n\nLIKELY CAUSES:\n 1.   This exception may have been caused by ineffecient read/write permissions with the \nspecified root folder. If this is the case, modify permissions and rerun Packin.\n\n2.   The root folder that you specified may not exist and/or was specified incorrectly.");
        }
        ProgressBar.setValue(7);
        try {
            FileWriter writer = new FileWriter(params[6] + "/DEBIAN/control");
            writer.write("Package: " + params[2].trim() + "\n");
            writer.write("Version: " + params[3].trim() + "\n");
            writer.append("Architecture: " + params[12].trim() + "\n");
            writer.write("Depends: " + params[7].trim() + "\n");
            writer.write("Pre-Depends: " + params[8].trim() + "\n");
            writer.write("Recommends: " + params[9].trim() + "\n");
            writer.write("Maintainer: " + params[0].trim() + " [" + params[1].trim() + "]\n");
            writer.write("Conflicts: " + params[10].trim() + "\n");
            writer.write("Replaces: " + params[11].trim() + "\n");
            writer.write("Provides: " + params[2].trim() + "\n");
            params[5] = params[5].trim();
            params[5] = params[5].replaceAll("\n", "\n             ");
            writer.write("Description: " + params[5] + "\n\n");
            writer.close();
            ProgressBar.setValue(10);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
        }
        String command = "";
        String location = "";
        String s = null;
        params[2] = params[2].replaceAll(" ", "");
        try {
            int i1 = params[6].lastIndexOf("/");
            String store = params[6].substring(0, i1);
            command = "dpkg -b " + params[6] + " " + store.trim() + "/" + params[2].trim() + "_" + params[3].trim() + "_" + params[12].trim() + ".deb";
            location = store.trim() + "/" + params[2].trim() + "_" + params[3].trim() + "_" + params[12].trim() + ".deb";
        } catch (Exception ex) {
            command = "dpkg -b " + params[6];
            location = "";
        }
        ProgressBar.setValue(15);
        try {
            StatusArea.setText(StatusArea.getText() + "Starting dpkg with command:\n" + command + "\n");
            Process p = Runtime.getRuntime().exec(command);
            ProgressBar.setValue(45);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StatusArea.setText(StatusArea.getText() + ":\n");
            while ((s = stdInput.readLine()) != null) {
                StatusArea.setText(StatusArea.getText() + s);
            }
            ProgressBar.setValue(86);
            StatusArea.setText(StatusArea.getText() + "\nAny errors or warnings listed:\n");
            int err = 0;
            while ((s = stdError.readLine()) != null) {
                StatusArea.setText(StatusArea.getText() + s);
                err = 1;
            }
            ProgressBar.setValue(95);
            if (err == 0) {
                if (location != "") {
                    JOptionPane.showMessageDialog(null, "Packin has successfully created the deb package. The package has been saved to:\n " + location);
                } else {
                    JOptionPane.showMessageDialog(null, "Packin has successfully created the deb package. The package has been stored one level above the specified root folder. It has been named deb.deb");
                }
            } else {
                JOptionPane.showMessageDialog(null, "An error or warning has occurred! Please refer to the status list in order to \nread more about the errors and/or warnings encountered.\n\nNOTE: The package may have been successfully created even with warnings. \nIt is recommended, however, that any warnings be resolved and the package rebuilt.");
            }
            ProgressBar.setValue(100);
        } catch (Exception ex) {
        }
    }
