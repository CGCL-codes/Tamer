    public void executeTrail() {
        Project p = projectProxy.getVariable();
        if (p == null) return;
        File saveFile = p.getSaveFile();
        JFileChooser fc;
        if (saveFile != null) fc = new JFileChooser(saveFile.getParentFile()); else fc = new JFileChooser();
        if (fc.showOpenDialog(common.getForegroundWindowOrNull()) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        BufferedReader bf = null;
        IProgress progressbar = common.getProgressBar("Executing trail...");
        progressbar.setScale(100);
        progressbar.start();
        try {
            HashMap<String, PrimaryDocument> filenames = new HashMap<String, PrimaryDocument>();
            for (PrimaryDocument pd : PrimaryDocument.getTreeWalker(p.getPrimaryDocuments())) {
                if (pd.getFilename() != null) filenames.put(pd.getFilename(), pd);
            }
            progressbar.work(5);
            if (progressbar.isCanceled()) return;
            try {
                bf = new BufferedReader(new InputStreamReader(new ProgressInputStream(f, progressbar.getSub(95))));
                String nextLine = bf.readLine();
                if (nextLine == null) return;
                boolean applyDespiteWarning = false;
                boolean applyDespiteWarningForAll = false;
                do {
                    if (progressbar.isCanceled()) return;
                    int spaces = getSpaces(nextLine);
                    if (spaces == 0) {
                        String[] firstLine = nextLine.split(" changed ");
                        if (firstLine.length == 2) {
                            String filename = firstLine[1];
                            PrimaryDocument pd = filenames.get(filename.trim());
                            if (pd == null) {
                                System.out.println("Could not find primary document: " + nextLine);
                                nextLine = bf.readLine();
                                continue;
                            }
                            String oldCode = null;
                            while (((nextLine = bf.readLine()) != null) && !((getSpaces(nextLine) == 2) && ((nextLine.trim().equals("Old:"))) || (nextLine.trim().equals("No old code")))) {
                            }
                            StringBuilder sb = new StringBuilder();
                            if (!nextLine.trim().equals("No old code")) {
                                while (((nextLine = bf.readLine()) != null) && (getSpaces(nextLine) >= 4)) {
                                    if (sb.length() != 0) sb.append('\n');
                                    sb.append(nextLine.substring(4));
                                }
                                oldCode = sb.toString();
                            }
                            String newcode = null;
                            while (nextLine != null && !((getSpaces(nextLine) == 2) && (nextLine.trim().equals("New:")) || (nextLine.trim().equals("No new code")))) {
                                nextLine = bf.readLine();
                            }
                            if (!nextLine.trim().equals("No new code")) {
                                StringBuilder sb2 = new StringBuilder();
                                while (((nextLine = bf.readLine()) != null) && (getSpaces(nextLine) >= 4)) {
                                    if (sb2.length() != 0) sb2.append('\n');
                                    sb2.append(nextLine.substring(4));
                                }
                                newcode = sb2.toString();
                            }
                            String pdoldcode = pd.getCodeAsString();
                            if (!ObjectUtils.equals(pdoldcode, oldCode)) {
                                if (!applyDespiteWarningForAll) {
                                    System.out.println("Mismatch in " + filename + "\n" + "Code from trail:\n" + CStringUtils.indent(oldCode, 2) + "\n" + "Code in project:\n" + CStringUtils.indent(pdoldcode, 2) + "\n" + "New code:\n" + CStringUtils.indent(newcode, 2) + "\n");
                                    switch(JOptionPane.showOptionDialog(common.getForegroundWindowOrNull(), "Code in trail file does not match existing code in project file.\n" + "Code from trail:\n" + CStringUtils.indent(oldCode.substring(0, Math.min(100, oldCode.length())), 2) + "\n" + "Code in project:\n" + CStringUtils.indent(pdoldcode.substring(0, Math.min(100, oldCode.length())), 2) + "\n" + "New code:\n" + CStringUtils.indent(newcode.substring(0, Math.min(100, oldCode.length())), 2) + "\n" + "Apply change nevertheless?", "Executing Trail...", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[] { "Yes", "Yes to all", "No", "No to all", "Cancel" }, 0)) {
                                        case 0:
                                            applyDespiteWarning = true;
                                            break;
                                        case 1:
                                            applyDespiteWarning = true;
                                            applyDespiteWarningForAll = true;
                                            break;
                                        case 2:
                                            applyDespiteWarning = false;
                                            break;
                                        case 3:
                                            applyDespiteWarning = false;
                                            applyDespiteWarningForAll = true;
                                        default:
                                            return;
                                    }
                                }
                                if (applyDespiteWarning) {
                                    pd.setCode(newcode);
                                } else {
                                    System.out.println("Unapplied change to PD: " + pd.getFilename() + "\n" + "Code from trail:\n" + CStringUtils.indent(oldCode, 2) + "\n" + "Code in project:\n" + CStringUtils.indent(pdoldcode, 2) + "\n" + "New code:\n" + CStringUtils.indent(newcode, 2));
                                }
                            } else {
                                pd.setCode(newcode);
                            }
                            continue;
                        }
                        firstLine = nextLine.substring(nextLine.indexOf("'") + 1).split("' to '");
                        if (firstLine.length == 2) {
                            String fromCode = firstLine[0];
                            String toCode = firstLine[1].substring(0, firstLine[1].lastIndexOf("\'"));
                            while (((nextLine = bf.readLine()) != null) && (getSpaces(nextLine) == 2)) {
                                PrimaryDocument pd = filenames.get(nextLine.trim());
                                if (pd != null) pd.renameCodes(fromCode, toCode);
                            }
                            continue;
                        }
                    }
                    nextLine = bf.readLine();
                } while (nextLine != null);
            } catch (IOException e) {
                throw new ReportToUserException(e);
            }
        } finally {
            progressbar.done();
            if (bf != null) try {
                bf.close();
            } catch (IOException e) {
            }
        }
    }
