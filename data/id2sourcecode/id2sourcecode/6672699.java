    public void crawl(String site, String[] parsers) {
        LinkedList<Parser> parserList = new LinkedList<Parser>();
        for (int i = 0; i < parsers.length; ++i) {
            for (int j = 0; j < parser.length; ++j) {
                if (parser[j].getName().contentEquals(parsers[i])) {
                    parserList.add(parser[j]);
                    break;
                }
            }
        }
        try {
            if (cache) {
                java.io.FileInputStream inputFile = new java.io.FileInputStream(site);
                byte[] buffer = new byte[10240];
                java.io.ByteArrayOutputStream data_dump = new java.io.ByteArrayOutputStream();
                int num_read = -1;
                while ((num_read = inputFile.read(buffer)) > 0) {
                    data_dump.write(buffer, 0, num_read);
                }
                inputFile.close();
                java.io.ByteArrayInputStream source = new java.io.ByteArrayInputStream(data_dump.toByteArray());
                for (Parser p : parserList) {
                    try {
                        if (spidering) {
                            p.parse(source, crawler, site);
                        } else {
                            p.parse(source, site);
                        }
                    } catch (Exception e) {
                        Logger.getLogger(FileListCrawler.class.getName()).log(Level.SEVERE, "Exception in File " + site + ": " + e.getMessage());
                    }
                    source.mark(0);
                }
                source.close();
            } else {
                for (Parser p : parserList) {
                    java.io.FileInputStream inputFile = new java.io.FileInputStream(site);
                    try {
                        if (spidering) {
                            p.parse(inputFile, crawler, site);
                        } else {
                            p.parse(inputFile, site);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(FileListCrawler.class.getName()).log(Level.SEVERE, "Exception in " + site + ": " + ex.getMessage());
                    } finally {
                        try {
                            inputFile.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FileListCrawler.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(FileListCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
    }
