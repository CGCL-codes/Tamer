        public void run() {
            boolean CoDaPackUpdaterUpdated = false;
            JSONObject data = null;
            try {
                URL url = new URL(CoDaPackConf.HTTP_ROOT + "codapack-updater.json");
                InputStreamReader isr = new InputStreamReader(url.openStream());
                try {
                    data = new JSONObject(new BufferedReader(isr).readLine());
                } catch (JSONException ex) {
                    System.out.println("Error reading the JSON file");
                }
                isr.close();
                if (data != null) {
                    int updaterVersion = 0;
                    try {
                        updaterVersion = data.getInt("updater-version");
                    } catch (JSONException ex) {
                        Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (CoDaPackConf.CoDaUpdaterVersion < updaterVersion) {
                        int response = JOptionPane.showConfirmDialog(null, "CoDaPack Updater needs to be updated", "Update confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (response == JOptionPane.YES_OPTION) {
                            url = new URL(CoDaPackConf.HTTP_ROOT + "codapack/CoDaPackUpdater.jar");
                            InputStream is = url.openStream();
                            FileOutputStream fos = null;
                            File tempFile = new File("CoDaPackUpdater.jar_temp");
                            fos = new FileOutputStream(tempFile);
                            int oneChar;
                            while ((oneChar = is.read()) != -1) {
                                fos.write(oneChar);
                            }
                            is.close();
                            fos.close();
                            (new File("CoDaPackUpdater.jar")).delete();
                            boolean result = tempFile.renameTo(new File("CoDaPackUpdater.jar"));
                            CoDaPackConf.CoDaUpdaterVersion = updaterVersion;
                            CoDaPackConf.saveConfiguration();
                            CoDaPackUpdaterUpdated = true;
                        }
                    } else {
                        CoDaPackUpdaterUpdated = true;
                    }
                } else {
                    CoDaPackUpdaterUpdated = false;
                }
            } catch (IOException ex) {
                CoDaPackUpdaterUpdated = false;
                System.out.println("Some problem connecting to IMA server");
            }
            if (CoDaPackUpdaterUpdated == true) {
                try {
                    if (CoDaPackConf.updateNeeded(data.getString("codapack-version"))) {
                        int response = JOptionPane.showConfirmDialog(null, "There is a new version of CoDaPack available. Do you want to install it? If you say yes, CoDaPack is going to be closed automatically.", "Update confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (response == JOptionPane.YES_OPTION) {
                            try {
                                Process ps = Runtime.getRuntime().exec("java -jar CoDaPackUpdater.jar");
                                System.exit(0);
                            } catch (IOException ex) {
                                System.out.println("It's not possible to execute the updater");
                            }
                        }
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(CoDaPackMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
