    @Override
    public void start() {
        TarInputStream input = null;
        int count = 1;
        try {
            input = new TarInputStream(new GZIPInputStream(new FileInputStream((File) properties.get("ProfileFile").get())));
            LastFMUserExpansion userExpansion = new LastFMUserExpansion();
            Parser[] parsers = userExpansion.setUpParsers(null, graph);
            TarEntry entry = input.getNextEntry();
            while (entry != null) {
                if (count % 1000 == 0) {
                    Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.INFO, "Reading file " + count);
                }
                String name = entry.getName();
                String[] pieces = name.split("/");
                if (!entry.isDirectory() && pieces.length > 1) {
                    ByteArrayOutputStream copyOfFile = new ByteArrayOutputStream();
                    input.copyEntryContents(copyOfFile);
                    ByteArrayInputStream data = new ByteArrayInputStream(copyOfFile.toByteArray());
                    if (pieces[0].contentEquals("ArtistDirectory")) {
                        try {
                            parsers[4].parse(data, entry.getFile().getAbsolutePath());
                        } catch (Exception ex) {
                            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                        }
                    } else {
                        if (pieces[2].contentEquals("friends.xml")) {
                            try {
                                parsers[5].parse(data, entry.getFile().getAbsolutePath());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("topArtists.xml")) {
                            try {
                                parsers[1].parse(data, entry.getFile().getAbsolutePath());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("profile.xml")) {
                            try {
                                parsers[0].parse(data, entry.getFile().getAbsolutePath());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("tags.xml")) {
                            try {
                                parsers[3].parse(data, entry.getFile().getAbsolutePath());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("neighbours.xml")) {
                            try {
                                parsers[6].parse(data, entry.getFile().getAbsolutePath());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else {
                            try {
                                parsers[2].parse(data, entry.getFile().getAbsolutePath());
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        }
                    }
                }
                count++;
                entry = input.getNextEntry();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
