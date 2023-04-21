    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("new browser")) {
            browseLibrary();
        } else if (e.getActionCommand().equals("new context")) {
            try {
                Library.createUserContext(currentFrame);
            } catch (Exception ex) {
                activity.log.append("While creating a new context, " + ex + "\n\n");
            }
            changeActivity(Library.DATA_GATHERING);
        } else if (e.getActionCommand().equals("Match dyadsUndefined")) {
            try {
                DomainTheory.current.matchDyads();
            } catch (Exception ex) {
                activity.log.append("While matching dyadsUndefined, " + ex + "\n\n");
            }
        } else if (e.getActionCommand().equals("open context")) {
            try {
                Library.chooseUserContext(currentFrame);
            } catch (Exception ex) {
                activity.log.append("While opening a new context, " + ex + "\n\n");
            }
            changeActivity(Library.DATA_GATHERING);
        } else if (e.getActionCommand().equals("open silk")) {
            if (fc == null) {
                fc = new JFileChooser();
            }
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setDialogTitle("File from KinshipEditor (.silk)");
            int returnVal = fc.showOpenDialog(desktop);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                changeActivity(Library.DATA_GATHERING);
                File file = fc.getSelectedFile();
                try {
                    Library.loadSILKFile(file);
                } catch (Exception ex) {
                    activity.log.append("While opening a KinshipEditor (.silk) file:\n" + ex + "\n\n");
                }
            }
        } else if (e.getActionCommand().equals("close window")) {
            KSJInternalFrame frame = (KSJInternalFrame) desktop.getSelectedFrame();
            frame.doDefaultCloseAction();
        } else if (e.getActionCommand().equals("exit")) {
            if (SIL_Edit.editWindow != null && SIL_Edit.editWindow.chart.dirty) {
                SIL_Edit.editWindow.chart.doWantToSave();
            }
            System.exit(0);
        } else if (e.getActionCommand().equals("delete from library")) {
            String[] langs = Library.genCtxtMenu("All");
            String victim = (String) JOptionPane.showInputDialog(desktop, "1)  For what language would you like to make a deletion?", "What To Delete", JOptionPane.PLAIN_MESSAGE, null, langs, langs[0]);
            if (victim == null) {
                return;
            }
            if (victim.equals("All")) {
                String vict;
                Library.ContextStub cs1;
                File aboutToDi;
                try {
                    for (int k = 1; k < langs.length; k++) {
                        vict = langs[k];
                        cs1 = Library.retrieveOrCreateStub(vict);
                        Library.removeContextStub(cs1);
                        aboutToDi = new File(Library.libraryCtxtDirectory + vict + ".ctxt");
                        aboutToDi.delete();
                    }
                    Library.cbIndex = null;
                    Library.writeStubFile();
                } catch (Exception exc) {
                    activity.log.append("While deleting Library files:\n" + exc + "\n\n");
                }
            } else {
                Library.ContextStub cs = Library.retrieveOrCreateStub(victim);
                Context actxt = (Context) Library.activeContexts.get(victim);
                String[] choices;
                if (cs.adrThyExists && cs.refThyExists) {
                    choices = new String[4];
                    choices[3] = "Both domain theories";
                    choices[1] = victim + " domain theory";
                    choices[2] = victim + " (Adr) domain theory";
                    choices[0] = "The entire " + victim + " context & its 2 domain theories";
                } else if (cs.refThyExists) {
                    choices = new String[2];
                    choices[1] = victim + " domain theory";
                    choices[0] = "The entire " + victim + " context and its domain theory";
                } else if (cs.adrThyExists) {
                    choices = new String[2];
                    choices[1] = victim + " (Adr) domain theory";
                    choices[0] = "The entire " + victim + " context and its domain theory";
                } else {
                    choices = new String[1];
                    choices[0] = "The entire " + victim + " context.  (It has no domain theory.)";
                }
                String choice = (String) JOptionPane.showInputDialog(desktop, "2)  Which elements of the context for that language \nwould you like to delete?", "What To Delete", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
                if (choice == null) {
                    return;
                }
                String[] onlyAlso = { "Card File ONLY", "Disk ALSO" };
                String eraser = (String) JOptionPane.showInputDialog(desktop, "3)  Delete from the Library's 'card file' only?\n Or delete the file from your computer also?", "What To Delete", JOptionPane.PLAIN_MESSAGE, null, onlyAlso, "Disk ALSO");
                if (eraser == null) {
                    return;
                }
                String filePath = Library.libraryCtxtDirectory + victim;
                File aboutToDie;
                try {
                    if (choice.substring(0, 10).equals("The entire")) {
                        Library.removeContextStub(cs);
                        Library.writeStubFile();
                        if (eraser.substring(0, 4).equals("Disk")) {
                            aboutToDie = new File(filePath + ".ctxt");
                            aboutToDie.delete();
                            filePath = Library.thyDirectory + victim;
                            aboutToDie = new File(filePath + ".thy");
                            if (aboutToDie.exists()) {
                                aboutToDie.delete();
                            }
                            aboutToDie = new File(filePath + "(Adr).thy");
                            if (aboutToDie.exists()) {
                                aboutToDie.delete();
                            }
                        }
                    } else if (choice.substring(0, 4).equals("Both")) {
                        cs.refThyExists = false;
                        cs.adrThyExists = false;
                        if (actxt == null) {
                            actxt = Library.readContextFromDisk(filePath + ".ctxt");
                        }
                        actxt.domTheoryAdrNullify();
                        actxt.domTheoryRefNullify();
                        if (eraser.substring(0, 4).equals("Disk")) {
                            Library.saveContextToDisk(actxt);
                            filePath = Library.thyDirectory + victim;
                            aboutToDie = new File(filePath + ".thy");
                            if (aboutToDie.exists()) {
                                aboutToDie.delete();
                            }
                            aboutToDie = new File(filePath + "(Adr).thy");
                            if (aboutToDie.exists()) {
                                aboutToDie.delete();
                            }
                        }
                    } else if (choice.indexOf("(Adr)") >= 0) {
                        cs.adrThyExists = false;
                        if (actxt == null) {
                            actxt = Library.readContextFromDisk(filePath + ".ctxt");
                        }
                        actxt.domTheoryAdrNullify();
                        if (eraser.substring(0, 4).equals("Disk")) {
                            Library.saveContextToDisk(actxt);
                            filePath = Library.thyDirectory + victim;
                            aboutToDie = new File(filePath + "(Adr).thy");
                            if (aboutToDie.exists()) {
                                aboutToDie.delete();
                            }
                        }
                    } else {
                        cs.refThyExists = false;
                        if (actxt == null) {
                            actxt = Library.readContextFromDisk(filePath + ".ctxt");
                        }
                        actxt.domTheoryRefNullify();
                        if (eraser.substring(0, 4).equals("Disk")) {
                            Library.saveContextToDisk(actxt);
                            filePath = Library.thyDirectory + victim;
                            aboutToDie = new File(filePath + ".thy");
                            if (aboutToDie.exists()) {
                                aboutToDie.delete();
                            }
                        }
                    }
                } catch (Exception exc) {
                    activity.log.append("While deleting Library files:\n" + exc + "\n\n");
                }
            }
            for (int i = 0; i < MainPane.openBrowsers.size(); i++) {
                LibBrowser lb = (LibBrowser) MainPane.openBrowsers.get(i);
                lb.picker.refreshLangMenu();
            }
            JOptionPane.showMessageDialog(desktop, "Requested deletions are completed.", "Task Completed", JOptionPane.PLAIN_MESSAGE);
        } else if (e.getActionCommand().equals("add domain theory")) {
            if (fc == null) {
                fc = new JFileChooser();
            }
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setDialogTitle("File holding Domain Theory (Horn Clauses)");
            if (currDomTheoryDir != null) {
                fc.setCurrentDirectory(currDomTheoryDir);
            }
            int returnVal = fc.showOpenDialog(desktop);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    Library.loadNewDomTh(file);
                    DomainTheory dt = DomainTheory.current;
                    Context.simulation = false;
                    dt.ctxt.simDataGen = false;
                    String lang = dt.languageName, filePath;
                    for (int i = 0; i < MainPane.openBrowsers.size(); i++) {
                        LibBrowser lb = (LibBrowser) MainPane.openBrowsers.get(i);
                        lb.picker.refreshLangMenu();
                    }
                    ArrayList<Object> egoList = new ArrayList<Object>();
                    if (FeatureVectorObj.el_1_distn == null) {
                        Library.readDistributionsFromFile();
                    }
                    FeatureVectorObj fv1 = dt.computeFeatureVector(egoList);
                    Library.postDistributions(fv1);
                    filePath = Library.libraryDirectory + "Feature Vectors/" + fv1.languageName + ".fvect";
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
                    fv1.toDisk(out);
                    dt.findOverlappingTerms(egoList);
                    dt.findHiddenNeuterEgos();
                    currDomTheoryDir = fc.getCurrentDirectory();
                    filePath = Library.libraryDirectory + "Domain Theory Files/" + lang + ".thy";
                    out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
                    dt.toThyFile(out);
                    Library.saveContextToDisk(dt.ctxt);
                    JOptionPane.showMessageDialog(desktop, lang + " Successfully Added to Library.", "Task Completed", JOptionPane.PLAIN_MESSAGE);
                } catch (KSConstraintInconsistency exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Correct or remove this clause from domain theory.\n                 " + "Then delete this theory from the Library & re-add it.";
                    displayError(msg, "Horn Clause Error - Constraints", JOptionPane.ERROR_MESSAGE);
                } catch (KSBadHornClauseException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Correct or remove this clause from domain theory.\n                 " + "Then delete this theory from the Library & re-add it.";
                    displayError(msg, "Horn Clause Error", JOptionPane.ERROR_MESSAGE);
                } catch (KSParsingErrorException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Correct this line number & try again.";
                    JOptionPane.showMessageDialog(desktop, msg, "Parsing Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (ClassNotFoundException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Submit Bug Report.";
                    JOptionPane.showMessageDialog(desktop, msg, "Missing Class encountered", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (KSInternalErrorException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: To replace an existing theory, delete the old" + " version, then add the new version.";
                    JOptionPane.showMessageDialog(desktop, msg, "Duplicate Domain Theory", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (NotSerializableException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: Internal Error: " + exc + "\nRECOMMENDATION: Submit Bug Report.";
                    JOptionPane.showMessageDialog(desktop, msg, "Internal Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (IOException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: Writing out to Library, " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Check for disk, directory, or permissions problems.";
                    JOptionPane.showMessageDialog(desktop, msg, "File System Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (JavaSystemException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: Writing out to Library, " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Check for disk, directory, or permissions problems.";
                    JOptionPane.showMessageDialog(desktop, msg, "File System Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                }
            }
        } else if (e.getActionCommand().equals("add batch of domain theories")) {
            if (fc == null) {
                fc = new JFileChooser();
            }
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setDialogTitle("Directory holding Domain Theories");
            if (currDomTheoryDir != null) {
                fc.setCurrentDirectory(currDomTheoryDir.getParentFile());
            }
            String lang = "";
            DomainTheory dt = null;
            int returnVal = fc.showOpenDialog(desktop);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    File dir = fc.getSelectedFile();
                    if (!dir.isDirectory()) {
                        throw new JavaSystemException("Selected file is not a directory.");
                    }
                    int doFVs = JOptionPane.showConfirmDialog(this, "Compute Feature Vectors?");
                    int keep = JOptionPane.showConfirmDialog(this, "Keep Contexts Active?");
                    int overWrite = JOptionPane.showConfirmDialog(this, "Allow Over-Write of Existing DTs?");
                    Library.allowOverWrites = (overWrite == JOptionPane.YES_OPTION);
                    currDomTheoryDir = dir;
                    if (doFVs == JOptionPane.YES_OPTION && FeatureVectorObj.el_1_distn == null) {
                        Library.resetDistributions();
                    }
                    File[] batch = dir.listFiles();
                    for (int i = 0; i < batch.length; i++) {
                        File file = batch[i];
                        String fName = file.getName(), filePath, mod;
                        if (fName.indexOf(".") != 0) {
                            Library.loadNewDomTh(file);
                            dt = DomainTheory.current;
                            Context.current = dt.ctxt;
                            Context.simulation = false;
                            dt.ctxt.simDataGen = false;
                            if (keep == JOptionPane.NO_OPTION) {
                                lang = dt.languageName;
                                int end = lang.indexOf("(");
                                if (end > -1) {
                                    lang = lang.substring(0, end);
                                }
                                Library.activeContexts.remove(lang);
                            }
                            PrintWriter out;
                            ArrayList<Object> egoList = new ArrayList<Object>();
                            if (doFVs == JOptionPane.YES_OPTION) {
                                FeatureVectorObj fv1 = dt.computeFeatureVector(egoList);
                                filePath = Library.libraryDirectory + "Feature Vectors/" + dt.languageName + ".fvect";
                                out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
                                fv1.toDisk(out);
                            } else {
                                ClauseBody.priorPred = "";
                                ClauseBody.seqTotal = 0;
                                ClauseBody.dupTotal = 0;
                                Iterator ktdIter = dt.theory.values().iterator();
                                egoList = dt.maleAndFemaleCreatedHeThem();
                                while (ktdIter.hasNext()) {
                                    KinTermDef ktd = (KinTermDef) ktdIter.next();
                                    if (dt.printableTerm(ktd)) {
                                        ktd.assureExamplesGenerated(egoList);
                                        if (ktd.eqcSigExact == null) {
                                            ktd.makeSigStrings();
                                        }
                                    }
                                }
                                String pad = "";
                                if (++ClauseBody.seq < 10) {
                                    pad = "  ";
                                } else if (ClauseBody.seq < 100) {
                                    pad = " ";
                                }
                                System.out.println(ClauseBody.priorPred + ": " + pad + ClauseBody.seq + " - " + ClauseBody.dups + " = " + (ClauseBody.seq - ClauseBody.dups));
                                ClauseBody.seqTotal += ClauseBody.seq;
                                System.out.println("\nTotal clauses for " + dt.languageName + " is " + ClauseBody.seqTotal + " - " + ClauseBody.dupTotal + " = " + (ClauseBody.seqTotal - ClauseBody.dupTotal) + "\n\n");
                            }
                            dt.findOverlappingTerms(egoList);
                            dt.findHiddenNeuterEgos();
                            filePath = Library.libraryDirectory + "Domain Theory Files/" + dt.languageName + ".thy";
                            out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
                            dt.toThyFile(out);
                            System.out.println("Wrote: " + filePath);
                            Library.saveContextToDisk(Context.current);
                        }
                    }
                    for (int i = 0; i < MainPane.openBrowsers.size(); i++) {
                        LibBrowser lb = (LibBrowser) MainPane.openBrowsers.get(i);
                        lb.picker.refreshLangMenu();
                    }
                    int num = batch.length - 1;
                    JOptionPane.showMessageDialog(desktop, num + " DomTheories Added to Library.", "Task Completed", JOptionPane.PLAIN_MESSAGE);
                } catch (KSConstraintInconsistency exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM with " + dt.languageName + ": " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Correct or remove this clause from domain theory.\n                 " + "Then delete this theory from the Library & re-add it.";
                    JOptionPane.showMessageDialog(desktop, msg, "Horn Clause Error - Constraints", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (KSBadHornClauseException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM with " + dt.languageName + ": " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Correct or remove this clause from domain theory.\n                 " + "Then delete this theory from the Library & re-add it.";
                    JOptionPane.showMessageDialog(desktop, msg, "Horn Clause Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (KSParsingErrorException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM in Parsing: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Correct this line number & try again.";
                    JOptionPane.showMessageDialog(desktop, msg, "Parsing Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (ClassNotFoundException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM with " + dt.languageName + ": " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Submit Bug Report.";
                    JOptionPane.showMessageDialog(desktop, msg, "Missing Class encountered", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (KSInternalErrorException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM with " + DomainTheory.current.languageName + ": " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: To replace an existing theory, delete the old" + " version, then add the new version.";
                    JOptionPane.showMessageDialog(desktop, msg, "Duplicate Domain Theory", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (NotSerializableException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: Internal Error: " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Submit Bug Report.";
                    JOptionPane.showMessageDialog(desktop, msg, "Internal Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (IOException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: Writing out to Library, " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Check for disk, directory, or permissions problems.";
                    JOptionPane.showMessageDialog(desktop, msg, "File System Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                } catch (JavaSystemException exc) {
                    int where = exc.toString().indexOf(":");
                    String msg = "PROBLEM: Writing out to Library, " + exc.toString().substring(where + 1) + "\nRECOMMENDATION: Check for disk, directory, or permissions problems.";
                    JOptionPane.showMessageDialog(desktop, msg, "File System Error", JOptionPane.ERROR_MESSAGE);
                    activity.log.append(msg + "\n\n");
                }
            }
        } else if (e.getActionCommand().equals("delete context")) {
            try {
                Library.deleteContext(currentFrame);
            } catch (Exception ex) {
                activity.log.append("While deleting a context, " + ex + "\n\n");
            }
            changeActivity(Library.DATA_GATHERING);
        } else if (e.getActionCommand().equals("save context")) {
            if (SIL_Edit.editWindow != null) {
                SIL_Edit.editWindow.chart.saveSILKinFile();
            } else try {
                Library.saveUserContext(currentFrame, false);
            } catch (Exception ex) {
                activity.log.append("While saving a context, " + ex + "\n\n");
            }
            changeActivity(Library.DATA_GATHERING);
        } else if (e.getActionCommand().equals("save context as")) {
            if (SIL_Edit.editWindow != null) {
                SIL_Edit.editWindow.chart.saveAsFile();
            } else try {
                Library.saveUserContext(currentFrame, true);
            } catch (Exception ex) {
                activity.log.append("While saving a context, " + ex + "\n\n");
            }
            changeActivity(Library.DATA_GATHERING);
        } else if (e.getActionCommand().equals("export GEDCOM")) {
            int population = Context.current.indSerNumGen + Context.current.famSerNumGen;
            if (population < 2) {
                JOptionPane.showMessageDialog(desktop, "You are requesting export of a total of " + population + " people and families.\n" + "That makes no sense.  Make sure you have chosen the correct\n" + "context and generated a population before exporting.", "Nothing To Export", JOptionPane.ERROR_MESSAGE);
            } else {
                if (fc == null) {
                    fc = new JFileChooser();
                }
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.setDialogTitle("Location for GEDCOM file -- .ged extension recommended");
                if (currGEDCOMDir != null) {
                    fc.setCurrentDirectory(currGEDCOMDir);
                }
                int returnVal = fc.showSaveDialog(desktop);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    currGEDCOMDir = fc.getCurrentDirectory();
                    String fName = file.getName(), fPath = file.getPath();
                    Object[] options2 = { "Include", "Don't Include" };
                    int choice1 = 1;
                    choice1 = JOptionPane.showOptionDialog(desktop, "Some names have 'flags' like <aux> embedded in them.  Include those?", "Include Names With Tags?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options2, options2[1]);
                    try {
                        PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter(fPath)));
                        Context.current.exportGEDCOM(outFile, fName, (String) options2[choice1]);
                        outFile.flush();
                        outFile.close();
                    } catch (IOException exc) {
                        String msg = "PROBLEM: While exporting a GEDCOM file, " + prettify(exc.toString()) + "\nRECOMMENDATION: Check for disk, directory, or permissions problems.";
                        JOptionPane.showMessageDialog(desktop, msg, "File System Error", JOptionPane.ERROR_MESSAGE);
                        activity.log.append(msg + "\n\n");
                    } catch (Exception exc) {
                        String msg = "PROBLEM: While exporting a GEDCOM file, " + prettify(exc.toString()) + "\nRECOMMENDATION: Submit a Bug Report!";
                        JOptionPane.showMessageDialog(desktop, msg, "General Error", JOptionPane.ERROR_MESSAGE);
                        activity.log.append(msg + "\n\n");
                    }
                }
            }
        } else if (e.getActionCommand().equals("export domain theory")) {
        } else if (e.getActionCommand().equals("edit prefs")) {
            if (SIL_Edit.editWindow != null) {
                SIL_Edit.editWindow.editPrefsItemActionPerformed(null);
            } else {
                String msg = "You may only edit Prefs for a Context Under Construction." + "\nThere currently is none.";
                JOptionPane.showMessageDialog(desktop, msg, "Invalid Command", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
        } else if (e.getActionCommand().equals("edit user context")) {
            editCUC();
        } else if (e.getActionCommand().equals("Compute Similarity Matrix")) {
            try {
                Library.generateSimMatrix();
            } catch (Exception exc) {
                String msg = "PROBLEM: During FV generation, " + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
        } else if (e.getActionCommand().equals("Cluster the Feature Vectors")) {
            int minK, maxK;
            float penalty;
            try {
                String minKStr = JOptionPane.showInputDialog(this, "Enter Smallest Number of Clusters"), maxKStr = JOptionPane.showInputDialog(this, "Enter Largest Number of Clusters"), penaltyStr = JOptionPane.showInputDialog(this, "Enter Penalty Percentage (a whole number)");
                minK = Integer.parseInt(minKStr);
                maxK = Integer.parseInt(maxKStr);
                penalty = Float.parseFloat(penaltyStr) / 100f;
                Library.clusterFVs(minK, maxK, penalty);
            } catch (Exception exc) {
                String msg = "PROBLEM: During FV clustering, " + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
        } else if (e.getActionCommand().equals("Make Curr Lang dyadsUndefined")) {
            try {
                if (Context.current != null) {
                    System.out.println("Generating dyadsUndefined for " + Context.current.languageName);
                    DomainTheory.current.dyadsUndefined = new DyadTMap();
                    Context.current.addDyads((Individual) Context.current.individualCensus.get(0));
                }
            } catch (Exception exc) {
                String msg = "PROBLEM: During Dyad generation, " + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
        } else if (e.getActionCommand().equals("Gen Indexes")) {
            try {
                String startStr = JOptionPane.showInputDialog(this, "Start with what Language number?"), endStr = JOptionPane.showInputDialog(this, "End with what Language number (inclusive)?"), tag = JOptionPane.showInputDialog(this, "Suffix for this segment");
                int start = Integer.parseInt(startStr), end = Integer.parseInt(endStr);
                if (Library.predEncodings == null) {
                    Library.predEncodings = new TreeMap();
                }
                if (Library.predDecodings == null) {
                    Library.predDecodings = new TreeMap();
                }
                Library.parseClauseCounterOn = false;
                Library.generateAllIndexes(start, end, tag);
            } catch (RuntimeException rte) {
                throw new RuntimeException(rte);
            } catch (Exception exc) {
                String msg = "PROBLEM: During Index generation, " + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
        } else if (e.getActionCommand().equals("Merge Indexes")) {
            String[] suffixes = new String[10];
            int index = 0;
            String suffix = JOptionPane.showInputDialog(this, "What was the 1st Suffix used?  (Hit 'return' to cancel.)");
            if (suffix.equals("")) {
                return;
            }
            suffixes[index++] = suffix;
            while (!suffix.equals("")) {
                suffix = JOptionPane.showInputDialog(this, "What was the next Suffix used?  (Hit 'return' to end.)");
                suffixes[index++] = suffix;
            }
            index--;
            ArrayList<Object> cbIfiles = new ArrayList<Object>(), bcbIfiles = new ArrayList<Object>(), ktFiles = new ArrayList<Object>(), cbCountFiles = new ArrayList<Object>(), predEncodeFiles = new ArrayList<Object>(), predDecodeFiles = new ArrayList<Object>();
            String cbIbase = Library.libraryDirectory + "Index Segments/ClauseIndex", bcbIbase = Library.libraryDirectory + "Index Segments/BaseCBIndex", ktBase = Library.libraryDirectory + "Index Segments/KinTermSigTree", cbCountBase = Library.libraryDirectory + "Index Segments/ClauseCounts", predEncodeBase = Library.libraryDirectory + "Index Segments/PredEncodings", predDecodeBase = Library.libraryDirectory + "Index Segments/PredDecodings";
            for (int i = 0; i < index; i++) {
                cbIfiles.add(cbIbase + suffixes[i]);
                bcbIfiles.add(bcbIbase + suffixes[i]);
                ktFiles.add(ktBase + suffixes[i]);
                cbCountFiles.add(cbCountBase + suffixes[i]);
                predEncodeFiles.add(predEncodeBase + suffixes[i]);
                predDecodeFiles.add(predDecodeBase + suffixes[i]);
            }
            try {
                if (Library.predEncodings == null) {
                    Library.readPredEncodings();
                }
                if (Library.predDecodings == null) {
                    Library.readPredDecodings();
                }
                Library.mergeIndexes(cbIfiles, bcbIfiles, ktFiles, cbCountFiles, predEncodeFiles, predDecodeFiles);
            } catch (RuntimeException rte) {
                throw new RuntimeException(rte);
            } catch (Exception exc) {
                String msg = "PROBLEM: During merger of Index files,\n" + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
            return;
        } else if (e.getActionCommand().equals("Leave 1 Instant Replay")) {
            File file = null;
            try {
                if (fc == null) {
                    fc = new JFileChooser();
                }
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.setDialogTitle("Choose the test (.case file) to replay");
                int returnVal = fc.showOpenDialog(desktop);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                }
                if (file == null) {
                    return;
                }
                int nmbrOfRounds, maxNoise, ignorable, minDyadsPerPCStr, roundNmbr, start, where;
                BufferedReader inFile = new BufferedReader(new FileReader(file));
                String line, dtFileName = inFile.readLine();
                ObjectInputStream str = new ObjectInputStream(new FileInputStream(dtFileName));
                Learned_DT learner = (Learned_DT) str.readObject();
                DomainTheory sourceDT = (learner.languageName.contains("(Adr)") ? learner.ctxt.domTheoryAdr() : learner.ctxt.domTheoryRef());
                Context.current = learner.ctxt;
                nmbrOfRounds = Integer.parseInt(inFile.readLine());
                Library.baseCB_Wait = Integer.parseInt(inFile.readLine());
                Library.inductionWait = Integer.parseInt(inFile.readLine());
                int[] distanceLimits = new int[nmbrOfRounds + 1];
                line = inFile.readLine();
                start = 1;
                for (int i = 1; i <= nmbrOfRounds; i++) {
                    where = line.indexOf(",", start);
                    if (where == -1) {
                        where = line.length() - 1;
                    }
                    distanceLimits[i] = Integer.parseInt(line.substring(start, where));
                    start = where + 2;
                }
                int[] minHitPercents = new int[nmbrOfRounds + 1];
                line = inFile.readLine();
                start = 1;
                for (int i = 1; i <= nmbrOfRounds; i++) {
                    where = line.indexOf(",", start);
                    if (where == -1) {
                        where = line.length() - 1;
                    }
                    minHitPercents[i] = Integer.parseInt(line.substring(start, where));
                    start = where + 2;
                }
                maxNoise = Integer.parseInt(inFile.readLine());
                ignorable = Integer.parseInt(inFile.readLine());
                Context.spellingNoise = Float.parseFloat(inFile.readLine());
                Context.classNoise = Float.parseFloat(inFile.readLine());
                Oracle.spellingNoise = Float.parseFloat(inFile.readLine());
                Oracle.classNoise = Float.parseFloat(inFile.readLine());
                Library.ClusterState.wtVectChoice = Integer.parseInt(inFile.readLine());
                minDyadsPerPCStr = Integer.parseInt(inFile.readLine());
                roundNmbr = Integer.parseInt(inFile.readLine());
                float priorMinHitPercent = Library.minHitPerCent;
                Context.simulation = true;
                if (FeatureVectorObj.el_1_distn == null) {
                    Library.readDistributionsFromFile();
                }
                String fileName = Library.libraryDirectory + "Test Cases/-Logs & Charts/Test-" + ++testSerialNmbr + ".log", tabFileName = Library.libraryDirectory + "Test Cases/-Logs & Charts/Test-" + testSerialNmbr + ".txt";
                PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(fileName))), tabFile = new PrintWriter(new BufferedWriter(new FileWriter(tabFileName)));
                Library.parseClauseCounterOn = false;
                leaveOneOut(learner, sourceDT, logFile, tabFile, nmbrOfRounds, distanceLimits, minHitPercents, maxNoise, ignorable, minDyadsPerPCStr, true, roundNmbr);
                logFile.flush();
                logFile.close();
                tabFile.flush();
                tabFile.close();
                Library.parseClauseCounterOn = true;
                Library.minHitPerCent = priorMinHitPercent;
                Library.contextUnderConstruction = null;
                return;
            } catch (RuntimeException rte) {
                throw rte;
            } catch (Exception exc) {
                String msg = "PROBLEM: During Leave-1-Instant-Replay,\n" + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
            return;
        } else if (e.getActionCommand().equals("Leave One Out")) {
            File file = null;
            try {
                if (fc == null) {
                    fc = new JFileChooser();
                }
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.setDialogTitle("Choose the theory file to parse.");
                int returnVal = fc.showOpenDialog(desktop);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                }
                if (file == null) {
                    return;
                }
                String fileName = file.getAbsolutePath();
                Library.readThyFile(fileName);
            } catch (Exception exc) {
            }
        } else if (e.getActionCommand().equals("Simulate User Data")) {
            try {
                String[] langs = Library.genCtxtMenu();
                String msg = "What context to use.", langChoice = null;
                boolean goodChoice = false;
                while (!goodChoice) {
                    langChoice = JOptionPane.showInputDialog(msg);
                    if (langChoice == null || langChoice.equals("")) {
                        return;
                    }
                    for (int i = 0; i < langs.length; i++) {
                        if (langChoice.equals(langs[i])) {
                            goodChoice = true;
                        }
                    }
                    if (!goodChoice) {
                        msg = "Sorry -- " + langChoice + " does not match any context name in the Library.";
                    }
                }
                Context ctxt = (Context) Library.activeContexts.get(langChoice);
                if (ctxt == null) {
                    String fileName = Library.libraryCtxtDirectory + langChoice + ".ctxt";
                    ctxt = Library.readContextFromDisk(fileName);
                }
                if (ctxt.individualCensus.size() > 0) {
                    msg = "There are " + ctxt.individualCensus.size() + " people already present.  Delete & start fresh?";
                    int killer = JOptionPane.showConfirmDialog(null, msg);
                    if (killer == JOptionPane.YES_OPTION) {
                        ctxt.resetTo(0, 0);
                        if (ctxt.domTheoryRefExists()) {
                            ctxt.domTheoryRef().dyadsUndefined = new DyadTMap();
                        }
                        if (ctxt.domTheoryAdrExists()) {
                            ctxt.domTheoryAdr().dyadsUndefined = new DyadTMap();
                        }
                        ctxt.ktm = new KinTermMatrix();
                    }
                } else {
                    ctxt.ktm = new KinTermMatrix();
                    if (ctxt.domTheoryRefExists()) {
                        ctxt.domTheoryRef().dyadsUndefined = new DyadTMap();
                    }
                    if (ctxt.domTheoryAdrExists()) {
                        ctxt.domTheoryAdr().dyadsUndefined = new DyadTMap();
                    }
                }
                String nmbrOf = JOptionPane.showInputDialog("Number of Egos to use? (enter a number)"), badSp = JOptionPane.showInputDialog("Percent Spelling Noise? (% entered as whole number)"), badCl = JOptionPane.showInputDialog("Percent Class Name Noise? (% entered as whole number)");
                NUMBER_OF_EGOS = Integer.parseInt(nmbrOf);
                Context.spellingNoise = Integer.parseInt(badSp) / 100f;
                Context.classNoise = Integer.parseInt(badCl) / 100f;
                Context.simulation = true;
                ArrayList<Object> egoBag = null;
                if (ctxt.domTheoryRefExists()) {
                    DomainTheory dt = ctxt.domTheoryRef();
                    Learned_DT learner = new Learned_DT(dt);
                    egoBag = dt.maleAndFemaleCreatedHeThem();
                    generateSimulatedData(ctxt, dt, 9, egoBag, learner);
                }
                if (ctxt.domTheoryAdrExists()) {
                    DomainTheory dt = ctxt.domTheoryAdr();
                    Learned_DT learner = new Learned_DT(dt);
                    if (egoBag == null) {
                        egoBag = dt.maleAndFemaleCreatedHeThem();
                    }
                    generateSimulatedData(ctxt, dt, 9, egoBag, learner);
                }
                if (!ctxt.domTheoryRefExists() && !ctxt.domTheoryAdrExists()) {
                    JOptionPane.showMessageDialog(null, "Could not simulate data -- there are no domain theories in this context.", "Serious Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Library.contextUnderConstruction = ctxt;
                }
            } catch (RuntimeException rte) {
                throw new RuntimeException(rte);
            } catch (Exception exc) {
                String msg = "PROBLEM: During User Simulation,\n" + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
            return;
        } else if (e.getActionCommand().equals("Gen CUC Population")) {
            try {
                Context cuc = Library.contextUnderConstruction;
                Context.current = cuc;
                DomainTheory.current = cuc.domTheoryRef();
                Individual indie;
                String gender;
                for (int j = 0; j < 20; j++) {
                    if (j % 2 == 0) {
                        gender = "F";
                    } else {
                        gender = "M";
                    }
                    indie = new Individual("Person#" + j, gender);
                }
            } catch (Exception exc) {
                String msg = "PROBLEM: During generation of a fake population, " + prettify(exc.toString()) + "\nRECOMMENDATION: DeBug!";
                JOptionPane.showMessageDialog(desktop, msg, "Testing Error", JOptionPane.ERROR_MESSAGE);
                activity.log.append(msg + "\n\n");
            }
        }
    }
