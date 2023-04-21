    public void crawl(String site, String[] parsers) throws MalformedURLException, IOException {
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
            if (!cache) {
                for (Parser p : parserList) {
                    java.util.zip.GZIPInputStream inputFile = new java.util.zip.GZIPInputStream(new java.io.FileInputStream(site), 102400);
                    try {
                        if (spidering) {
                            p.parse(inputFile, this, site);
                        } else {
                            p.parse(inputFile, site);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(GZipFileCrawler.class.getName()).log(Level.SEVERE, "Exception in " + site + ": " + ex.getMessage());
                    } finally {
                        try {
                            inputFile.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            } else {
                java.util.zip.GZIPInputStream inputFile = new java.util.zip.GZIPInputStream(new java.io.FileInputStream(site), 102400);
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
                            p.parse(source, this, site);
                        } else {
                            p.parse(source, site);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(GZipFileCrawler.class.getName()).log(Level.SEVERE, "Exception in " + site + ": " + ex.getMessage());
                    }
                    source.mark(0);
                }
                source.close();
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(GZipFileCrawler.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(GZipFileCrawler.class.getName()).log(Level.SEVERE, null, e);
        }
    }
