    private void load() {
        String[] nextLine = null;
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(INPUT_FILE_NAME));
            nextLine = reader.readNext();
            while (nextLine != null) {
                System.out.println("*** idKey: " + nextLine[IdentifyLifeHarvester.IDLIFE_IDX.ID.ordinal()] + ", taxon: " + nextLine[IdentifyLifeHarvester.IDLIFE_IDX.TAXONOMICSCOPE.ordinal()]);
                List<IdentificationKey> idKeyList = new ArrayList<IdentificationKey>();
                IdentificationKey idKey = toIdentificationKey(nextLine);
                nextLine = reader.readNext();
                if (idKey != null) {
                    logger.warn("*** guid: " + idKey.getIdentifier());
                    idKeyList.add(idKey);
                    while (nextLine != null) {
                        System.out.println("*** idKey: " + nextLine[IdentifyLifeHarvester.IDLIFE_IDX.ID.ordinal()] + ", taxon: " + nextLine[IdentifyLifeHarvester.IDLIFE_IDX.TAXONOMICSCOPE.ordinal()]);
                        IdentificationKey nextKey = toIdentificationKey(nextLine);
                        if (nextKey != null && idKey.getIdentifier().equals(nextKey.getIdentifier())) {
                            idKeyList.add(nextKey);
                            nextLine = reader.readNext();
                        } else {
                            break;
                        }
                    }
                }
                if (!idKeyList.isEmpty() && idKey.getIdentifier() != null && idKey.getIdentifier().length() > 0) {
                    try {
                        taxonConceptDao.addIdentificationKeys(idKey.getIdentifier(), idKeyList);
                    } catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
    }
