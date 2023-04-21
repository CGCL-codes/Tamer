    public void importSequences(WaitingHandler waitingHandler, ProteomicAnalysis proteomicAnalysis, File fastaFile, IdFilter idFilter, SearchParameters searchParameters) {
        try {
            waitingHandler.appendReport("Importing sequences from " + fastaFile.getName() + ".");
            waitingHandler.setSecondaryProgressDialogIntermediate(false);
            sequenceFactory.loadFastaFile(fastaFile, waitingHandler.getSecondaryProgressBar());
            String firstAccession = sequenceFactory.getAccessions().get(0);
            if (sequenceFactory.getHeader(firstAccession).getDatabaseType() != DatabaseType.UniProt) {
                showDataBaseHelpDialog();
            }
            if (!sequenceFactory.concatenatedTargetDecoy()) {
                waitingHandler.displayMessage("PeptideShaker validation requires the use of a taget-decoy database.\n" + "Some features will be limited if using other types of databases.\n\n" + "Note that using Automatic Decoy Search in Mascot is not supported.\n\n" + "See the PeptideShaker home page for details.", "No Decoys Found", JOptionPane.INFORMATION_MESSAGE);
            }
            waitingHandler.resetSecondaryProgressBar();
            waitingHandler.setSecondaryProgressDialogIntermediate(true);
            if (needPeptideMap) {
                if (2 * sequenceFactory.getNTargetSequences() < sequenceFactory.getnCache()) {
                    waitingHandler.appendReport("Creating peptide to protein map.");
                    Enzyme enzyme = searchParameters.getEnzyme();
                    if (enzyme == null) {
                        throw new NullPointerException("Enzyme not found");
                    }
                    int nMissedCleavages = searchParameters.getnMissedCleavages();
                    int nMin = idFilter.getMinPepLength();
                    int nMax = idFilter.getMaxPepLength();
                    sharedPeptides = new HashMap<String, ArrayList<String>>();
                    HashMap<String, String> tempMap = new HashMap<String, String>();
                    int numberOfSequences = sequenceFactory.getAccessions().size();
                    waitingHandler.setSecondaryProgressDialogIntermediate(false);
                    waitingHandler.setMaxSecondaryProgressValue(numberOfSequences);
                    for (String proteinKey : sequenceFactory.getAccessions()) {
                        waitingHandler.increaseSecondaryProgressValue();
                        String sequence = sequenceFactory.getProtein(proteinKey).getSequence();
                        for (String peptide : enzyme.digest(sequence, nMissedCleavages, nMin, nMax)) {
                            ArrayList<String> proteins = sharedPeptides.get(peptide);
                            if (proteins != null) {
                                proteins.add(proteinKey);
                            } else {
                                String tempProtein = tempMap.get(peptide);
                                if (tempProtein != null) {
                                    ArrayList<String> tempList = new ArrayList<String>(2);
                                    tempList.add(tempProtein);
                                    tempList.add(proteinKey);
                                    sharedPeptides.put(peptide, tempList);
                                } else {
                                    tempMap.put(peptide, proteinKey);
                                }
                            }
                        }
                        if (waitingHandler.isRunCanceled()) {
                            return;
                        }
                    }
                    tempMap.clear();
                    waitingHandler.setSecondaryProgressDialogIntermediate(true);
                } else {
                    waitingHandler.appendReport("The database is too large to be parsed into peptides. Note that X!Tandem peptides might present protein inference issues.");
                }
            }
            waitingHandler.appendReport("FASTA file import completed.");
            waitingHandler.increaseProgressValue();
        } catch (FileNotFoundException e) {
            waitingHandler.appendReport("File " + fastaFile + " was not found. Please select a different FASTA file.");
            e.printStackTrace();
            waitingHandler.setRunCanceled();
        } catch (IOException e) {
            waitingHandler.appendReport("An error occured while loading " + fastaFile + ".");
            e.printStackTrace();
            waitingHandler.setRunCanceled();
        } catch (IllegalArgumentException e) {
            waitingHandler.appendReport(e.getLocalizedMessage() + "\n" + "Please refer to the troubleshooting section at http://peptide-shaker.googlecode.com.");
            e.printStackTrace();
            waitingHandler.setRunCanceled();
        } catch (ClassNotFoundException e) {
            waitingHandler.appendReport("Serialization issue while processing the FASTA file. Please delete the .fasta.cui file and retry.\n" + "If the error occurs again please report bug at http://peptide-shaker.googlecode.com.");
            e.printStackTrace();
            waitingHandler.setRunCanceled();
        } catch (NullPointerException e) {
            waitingHandler.appendReport("The enzyme to use was not found.\n" + "Please verify the Search Parameters given while creating the project.\n" + "If the enzyme does not appear, verify that it is implemented in peptideshaker_enzymes.xml located in the conf folder of the PeptideShaker folder.\n\n" + "If the error persists please report bug at http://peptide-shaker.googlecode.com.");
            e.printStackTrace();
            waitingHandler.setRunCanceled();
        }
    }
