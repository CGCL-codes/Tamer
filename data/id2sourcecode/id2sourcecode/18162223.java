    public boolean start() {
        try {
            URL url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/reldate.txt");
            URLConnection urlc = url.openConnection();
            InputStream is = urlc.getInputStream();
            Reader r = new InputStreamReader(is);
            StringBuilder inputRelNotes = new StringBuilder();
            int i;
            while ((i = r.read()) != -1) {
                inputRelNotes.append((char) i);
            }
            String lRelNotes = inputRelNotes.toString();
            this.parseReleaseNotes(lRelNotes);
            System.out.println("Parsed the release notes");
            if (iTaxonomyFilter != null) {
                iFileName = "sprot_" + iSprotRelease + "_" + iTaxonomyFilter + ".fasta";
            } else {
                iFileName = "sprot_" + iSprotRelease + ".fasta";
            }
            BufferedWriter lDbOutputWriter = new BufferedWriter(new FileWriter(new File(iFolderLocation, iFileName)));
            URL urlDbDownload = new URL("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot.fasta.gz");
            URLConnection urlcDbDownload = urlDbDownload.openConnection();
            InputStream isDbDownload = urlcDbDownload.getInputStream();
            GZIPInputStream lZippedInputstream = new GZIPInputStream(isDbDownload);
            System.out.println("Downloading and unzipping the database");
            int lProteinCounter = 0;
            int entry;
            String lHeader = "";
            String lSequence = "";
            boolean lParsingHeader = false;
            boolean lParsingSequence = false;
            while ((entry = lZippedInputstream.read()) != -1) {
                if ((char) entry == '\n') {
                    if (lParsingHeader) {
                        lParsingHeader = false;
                        lParsingSequence = true;
                    }
                } else if ((char) entry == '>') {
                    if (lParsingSequence) {
                        lParsingHeader = true;
                        lParsingSequence = false;
                        if (this.writeProtein(lDbOutputWriter, lHeader, lSequence)) {
                            iProteinUsedCounter++;
                        }
                        lHeader = "";
                        lSequence = "";
                        lProteinCounter++;
                        if (lProteinCounter % 100 == 0) {
                            if (iProgressBar != null) {
                                if (iTaxonomyFilter != null) {
                                    iProgressBar.setString("Selected and written '" + iProteinUsedCounter + "' " + iTaxonomyFilter + " proteins of the totale " + lProteinCounter + " checked Swiss-Prot proteins!");
                                } else {
                                    iProgressBar.setString("Written " + lProteinCounter + " Swiss-Prot proteins!");
                                }
                            }
                        }
                        if (lProteinCounter % 500 == 0) {
                            System.out.print(".");
                            lDbOutputWriter.flush();
                        }
                        if (lProteinCounter % 40000 == 0) {
                            System.out.print("\n");
                            lDbOutputWriter.flush();
                        }
                    }
                    lParsingSequence = false;
                    lParsingHeader = true;
                    lHeader = lHeader + (char) entry;
                } else if (lParsingHeader) {
                    lHeader = lHeader + (char) entry;
                } else if (lParsingSequence) {
                    lSequence = lSequence + (char) entry;
                }
            }
            if (lHeader.length() != 0 && lSequence.length() != 0) {
                this.writeProtein(lDbOutputWriter, lHeader, lSequence);
            }
            lDbOutputWriter.flush();
            lDbOutputWriter.close();
            System.out.println("\nDone downloading and unzipping");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
