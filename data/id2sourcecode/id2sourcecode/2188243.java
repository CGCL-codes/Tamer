    public OperandToken evaluate(Token[] operands, GlobalValues globals) {
        String lineFile = "";
        boolean successB = true;
        globals.getInterpreter().displayText("UPDATING JMathLib\n");
        String updateSiteS = globals.getProperty("update.site.primary");
        globals.getInterpreter().displayText("update site: " + updateSiteS);
        String localVersionS = globals.getProperty("jmathlib.version");
        globals.getInterpreter().displayText("current version: " + localVersionS);
        globals.getInterpreter().displayText("Checking to which version an update is possible");
        URL url = null;
        try {
            url = new URL(updateSiteS + "?jmathlib_version=" + localVersionS + "&command=update");
        } catch (Exception e) {
            throwMathLibException("update: malformed url for getting update version");
            successB = false;
        }
        Properties props = new Properties();
        try {
            props.load(url.openStream());
        } catch (Exception e) {
            System.out.println("updates: Properties error");
            successB = false;
        }
        String updateVersionS = props.getProperty("update.toversion");
        String updateActionS = props.getProperty("update.action");
        if (updateActionS.equals("VERSION_UNKNOWN")) {
            globals.getInterpreter().displayText("Your version of JMathLib is not known by the update server.");
            return null;
        } else if (updateVersionS.equals("NO_UPDATE_AVAILABLE") || updateActionS.equals("NO_ACTION")) {
            globals.getInterpreter().displayText("No update available right now.");
            return null;
        } else if (updateActionS.equals("FULL_DOWNLOAD_REQUIRED")) {
            globals.getInterpreter().displayText("\n");
            globals.getInterpreter().displayText("Full download required in order to update!");
            globals.getInterpreter().displayText("Please visit www.jmathlib.de for details.");
            globals.getInterpreter().displayText("\n");
            String urlS = props.getProperty("update.full_file_url");
            String fileS = props.getProperty("update.file_name");
            globals.getInterpreter().displayText("url:  " + urlS);
            globals.getInterpreter().displayText("file: " + fileS);
            if ((urlS == null) || (fileS == null)) return null;
            Frame f = new Frame();
            FileDialog theFileDialog = new FileDialog(f, "Save to ...", FileDialog.SAVE);
            theFileDialog.setFile(fileS);
            theFileDialog.setVisible(true);
            String downloadDirS = theFileDialog.getDirectory();
            fileS = theFileDialog.getFile();
            if (downloadDirS == null) throwMathLibException("download directory error");
            if (fileS == null) throwMathLibException("download file error");
            globals.getInterpreter().setStatusText("You selected " + downloadDirS + " as download directory.");
            try {
                globals.getInterpreter().displayText("Downloading ...  (please wait some minutes)");
                URL fileURL = new URL(urlS);
                InputStream in = fileURL.openStream();
                File file = new File(downloadDirS, fileS);
                OutputStream out = new FileOutputStream(file);
                byte[] cbuf = new byte[4096];
                int len = -1;
                int x = 0;
                while ((len = in.read(cbuf)) != -1) {
                    out.write(cbuf, 0, len);
                    x += len;
                    globals.getInterpreter().setStatusText("downloaded " + new Integer(x).toString() + " bytes");
                }
                in.close();
                out.close();
                globals.getInterpreter().setStatusText("Downloading done.");
            } catch (Exception e) {
                successB = false;
                globals.getInterpreter().displayText("update: problem downloading " + fileS);
            }
            try {
                globals.getInterpreter().displayText("Running installer ...");
                Runtime.getRuntime().exec(downloadDirS + fileS);
                globals.getInterpreter().displayText("Please exit JMathLib");
            } catch (IOException exception) {
                throwMathLibException("SystemCommand");
            }
            return null;
        } else if (updateActionS.equals("INCREMENTAL_DOWNLOAD")) {
            globals.getInterpreter().displayText("updating to version >" + updateVersionS + "< \n");
            updateSiteS += "jmathlib_" + updateVersionS + "/";
            try {
                url = new URL(updateSiteS);
            } catch (Exception e) {
                throwMathLibException("update: malformed url");
                successB = false;
            }
            try {
                BufferedReader inR = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = null;
                while ((s = inR.readLine()) != null) {
                    if (s.startsWith("file")) {
                        String fileS = s.substring(5).trim();
                        globals.getInterpreter().displayText("new file: >" + fileS + "<");
                        try {
                            URL fileURL = new URL(updateSiteS + fileS);
                            InputStream in = fileURL.openStream();
                            File file = new File(globals.getWorkingDirectory(), fileS);
                            OutputStream out = new FileOutputStream(file);
                            byte[] cbuf = new byte[4096];
                            int len = -1;
                            while ((len = in.read(cbuf)) != -1) {
                                out.write(cbuf, 0, len);
                            }
                            in.close();
                            out.close();
                        } catch (Exception e) {
                            successB = false;
                            globals.getInterpreter().displayText("update: problem downloading " + fileS);
                        }
                    } else if (s.startsWith("dir")) {
                        String dirS = s.substring(4).trim();
                        globals.getInterpreter().displayText("new directory: >" + dirS + "<");
                        try {
                            File file = new File(globals.getWorkingDirectory(), dirS);
                            file.mkdir();
                        } catch (Exception e) {
                            successB = false;
                            globals.getInterpreter().displayText("update: problem creating directory " + dirS);
                        }
                    } else if (s.startsWith("del")) {
                        String delS = s.substring(4).trim();
                        globals.getInterpreter().displayText("delete file/dir: >" + delS + "<");
                        try {
                            File file = new File(globals.getWorkingDirectory(), delS);
                            file.delete();
                        } catch (Exception e) {
                            successB = false;
                            globals.getInterpreter().displayText("update: problem deleting " + delS);
                        }
                    } else if (s.startsWith("prop")) {
                        String propS = s.substring(5).trim();
                        globals.getInterpreter().displayText("new property: >" + propS + "<");
                        String[] p = propS.split("=");
                        globals.setProperty(p[0], p[1]);
                    } else {
                    }
                    if (!successB) break;
                }
            } catch (Exception e) {
                throwMathLibException("update: open or reading stream");
            }
        } else {
            throwMathLibException("update: unknown action");
        }
        if (!successB) globals.getInterpreter().displayText("\n Update was not successful. Repeat again later on or inform the admin.");
        return null;
    }
