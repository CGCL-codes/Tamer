        void installOrStartSync(OBRNode obrNode, boolean bStart) {
            BundleRecord br = obrNode.getBundleRecord();
            if (br == null) {
                return;
            }
            String updateURL = (String) br.getAttribute(BundleRecord.BUNDLE_UPDATELOCATION);
            Bundle b = getBundle(br);
            if (b != null) {
                boolean bIsRepoBundle = Util.isInRepo(b, updateURL);
                String[] options = bIsRepoBundle ? (new String[] { "Update", "Cancel" }) : (new String[] { "Update", "Install again", "Cancel" });
                String msg = bIsRepoBundle ? "The selected bundle is already installed\n" + "from the repository.\n" + "\n" + "It can be updated from the repository." : "The selected bundle is already installed.\n" + "\n" + "It can be updated from the repository, or\n" + "a new instance can be installed from the\n" + "repository";
                int n = JOptionPane.showOptionDialog(recordTree, msg, "Bundle is installed", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (bIsRepoBundle) {
                    if (n == 0) {
                        obrNode.appendLog("update.\n");
                    } else if (n == 1) {
                        return;
                    }
                } else {
                    if (n == 0) {
                        InputStream in = null;
                        try {
                            URL url = new URL(updateURL);
                            in = new BufferedInputStream(url.openStream());
                            b.update(in);
                            obrNode.appendLog("Updated from " + url + "\n");
                        } catch (Exception e) {
                            obrNode.appendLog("Update failed: " + e + "\n");
                        } finally {
                            try {
                                in.close();
                            } catch (Exception ignored) {
                            }
                        }
                        return;
                    } else if (n == 1) {
                        obrNode.appendLog("Install new instance.\n");
                    } else if (n == 2) {
                        return;
                    }
                }
            }
            BundleRepositoryService obr = getOBR();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            PrintStream outStream = new PrintStream(bout, true);
            boolean bResolve = true;
            try {
                boolean bOK = obr.deployBundle(outStream, outStream, updateURL, bResolve, bStart);
                if (bOK) {
                    if (sortCategory == SORT_STATUS) {
                        refreshList(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(outStream);
            } finally {
                try {
                    outStream.close();
                } catch (Exception ignored) {
                }
                String s = new String(bout.toByteArray());
                obrNode.appendLog(s);
            }
            setSelected(obrNode);
        }
