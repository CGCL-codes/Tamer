    private void saveFiles(File path, boolean hqs_only, boolean ins_only, boolean save_trace_file) {
        if (path == null) {
            return;
        }
        TableModel model = project_seqs_jtable.getModel();
        int successes = 0, save_column = project_seqs_jtable.getColumn(_model.SAVE_COLUMN_NAME).getModelIndex(), trace_name_column = project_seqs_jtable.getColumn(_model.TRACE_COLUMN_NAME).getModelIndex(), template_name_column = project_seqs_jtable.getColumn(_model.TEMPLATE_COLUMN_NAME).getModelIndex(), primer_name_column = project_seqs_jtable.getColumn(_model.PRIMER_COLUMN_NAME).getModelIndex(), path_column = project_seqs_jtable.getColumn(_model.PATH_COLUMN_NAME).getModelIndex(), project_column = -1;
        if (_per_plate) {
            project_column = project_seqs_jtable.getColumn(_model.PROJECT_COLUMN_NAME).getModelIndex();
        }
        boolean appendfile = false;
        boolean yesToAll = false;
        Object[] jOptions = { "Yes", "Yes to All", "No", "Cancel" };
        final int YES = 0;
        final int YESALL = 1;
        final int NO = 2;
        final int CANCEL = 3;
        String trace_name = "", seq = "", template_name = "", primer_name = "", trace_path = "", fasta_annot = "", project = "";
        File file = null;
        String mode = "";
        if (saveSingleFastaCheckBox.isSelected()) {
            saveAsFastaCheckBox.setSelected(true);
        }
        if (hqs_only) {
            mode = "[high qual only]";
        } else if (ins_only) {
            mode = "[high qual insert only]";
        } else if (save_trace_file) {
            saveAsFastaCheckBox.setSelected(false);
            saveSingleFastaCheckBox.setSelected(false);
        } else {
            mode = "[complete sequence]";
        }
        F: for (int row = 0; row < project_seqs_jtable.getRowCount(); ++row) {
            appendfile = false;
            if (((Boolean) model.getValueAt(row, save_column)).booleanValue()) {
                trace_name = (String) model.getValueAt(row, trace_name_column);
                template_name = (String) model.getValueAt(row, template_name_column);
                primer_name = (String) model.getValueAt(row, primer_name_column);
                trace_path = (String) model.getValueAt(row, path_column);
                if (_per_plate) {
                    project = (String) model.getValueAt(row, project_column);
                } else {
                    project = _project_name;
                }
                fasta_annot = trace_name + " " + mode + " project=" + project + " trace path=" + trace_path + " template=" + template_name + " primer=" + primer_name;
                String destination_name = new String(trace_name);
                if (saveWithTemplatePrimerInNameCheckBox.isSelected()) {
                    destination_name = template_name + "_" + primer_name + "_" + destination_name;
                }
                if (saveSingleFastaCheckBox.isSelected()) {
                    if (row == 0) {
                        appendfile = false;
                        destination_name = "fasta." + SEQ_TXT_FILE_SUFFIX;
                        file = new File(path, destination_name);
                    } else {
                        appendfile = true;
                    }
                } else {
                    if (save_trace_file) {
                        file = new File(path, destination_name + "." + TRACE_FILE_SUFFIX);
                    } else {
                        file = new File(path, destination_name + "." + SEQ_TXT_FILE_SUFFIX);
                    }
                }
                if (file.isDirectory()) {
                    JOptionPane.showMessageDialog(this, file + " is a directory.", "Aborting File Save Process", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!yesToAll && file.exists() && ((!saveSingleFastaCheckBox.isSelected()) || (saveSingleFastaCheckBox.isSelected() && row == 0))) {
                    int fileOverwriteChoice = JOptionPane.showOptionDialog(this, file + " already exists.\n" + "Overwrite?", "File Already Exists", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, jOptions, jOptions[2]);
                    if (fileOverwriteChoice == NO) {
                        int j = JOptionPane.showConfirmDialog(this, file + " already exists.\n" + "Save under a different name?", "Save Under a Different Name?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (j == JOptionPane.NO_OPTION) {
                            continue F;
                        } else {
                            boolean done = false;
                            while (!done) {
                                String new_name = JOptionPane.showInputDialog(this, "Enter a new name for " + file, "Save Under a Different Name", JOptionPane.DEFAULT_OPTION);
                                if (new_name == null) {
                                    continue F;
                                } else if (new_name.length() > 0) {
                                    if (save_trace_file) {
                                        if (!new_name.endsWith("." + TRACE_FILE_SUFFIX)) {
                                            new_name += "." + TRACE_FILE_SUFFIX;
                                        }
                                    } else {
                                        if (!new_name.endsWith("." + SEQ_TXT_FILE_SUFFIX)) {
                                            new_name += "." + SEQ_TXT_FILE_SUFFIX;
                                        }
                                    }
                                    File new_file = new File(file.getParent(), new_name);
                                    if (!new_file.exists()) {
                                        file = new_file;
                                        done = true;
                                    }
                                }
                            }
                        }
                    } else if (fileOverwriteChoice == CANCEL) {
                        break F;
                    } else if (fileOverwriteChoice == YESALL) {
                        yesToAll = true;
                    }
                }
                if (!save_trace_file) {
                    try {
                        seq = getSeqDbInterface().retrieveSeq(trace_name);
                    } catch (Exception ee) {
                        Utilities.unexpectedException(ee, this, CONTACT);
                    }
                }
                if (save_trace_file) {
                    successes += writeTraceFile(file, trace_name, trace_path);
                } else if (ins_only) {
                    SequenceFeature sf = null;
                    try {
                        sf = getSeqDbInterface().retrieveSequenceFeature(trace_name, CrossMatchOutput.NAME);
                    } catch (Exception ee) {
                        Utilities.unexpectedException(ee, this, CONTACT);
                    }
                    int nvf = sf.getFirst() - 1, nvl = sf.getLast() - 1;
                    if (nvf != nvl) {
                        successes += writeSequence(file, seq, fasta_annot, true, nvf, nvl, appendfile);
                    }
                } else if (hqs_only) {
                    SequenceFeature sf = null;
                    try {
                        sf = getSeqDbInterface().retrieveSequenceFeature(trace_name, QualSequence.HSR_NAME);
                    } catch (Exception ee) {
                        Utilities.unexpectedException(ee, this, CONTACT);
                    }
                    int hqf = sf.getFirst() - 1, hql = sf.getLast() - 1;
                    if (hqf != hql) {
                        successes += writeSequence(file, seq, fasta_annot, true, hqf, hql, appendfile);
                    }
                } else {
                    successes += writeSequence(file, seq, fasta_annot, false, 0, 0, appendfile);
                }
            }
        }
        if (successes > 0) {
            JOptionPane.showMessageDialog(this, "Saved " + successes + " sequences into " + "directory \"" + path + "\".", "Files Saved", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save files into " + "directory \"" + path + "\".", "No File Saved", JOptionPane.ERROR_MESSAGE);
        }
    }
