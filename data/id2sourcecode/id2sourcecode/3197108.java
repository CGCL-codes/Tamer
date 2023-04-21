    public List<MooreOrganism> findAllOrganisms() throws DaoException {
        int totalOrganisms = 0;
        try {
            URL url = new URL(HTTP_REQUEST);
            logger.info("Retrieving data from " + HTTP_REQUEST);
            List<MooreOrganism> allOrganisms = new ArrayList<MooreOrganism>();
            Scanner organismsScanner = new Scanner(url.openStream());
            organismsScanner.useDelimiter(ORGANISM_DELIMITER);
            Map<String, String> organismDataMap = new HashMap<String, String>();
            while (organismsScanner.hasNext()) {
                totalOrganisms++;
                organismDataMap.clear();
                String[] organismFieldData = getOrganismFieldData(organismsScanner.next(), totalOrganisms);
                for (int i = 0; i < organismFieldData.length; i++) {
                    extractFieldData(organismDataMap, organismFieldData[i], totalOrganisms, i + 1);
                }
                allOrganisms.add(createMooreOrganism(organismDataMap));
            }
            organismsScanner.close();
            logger.info("findAllOrganisms() returning " + allOrganisms.size() + " organisms");
            return allOrganisms;
        } catch (UnknownHostException e) {
            throw new DaoException(e, "findAllOrganisms() failed at organism=" + totalOrganisms);
        } catch (IOException e) {
            throw new DaoException(e, "findAllOrganisms() failed at organism=" + totalOrganisms);
        } catch (Exception e) {
            throw new DaoException(e, "findAllOrganisms() failed at organism=" + totalOrganisms);
        }
    }
