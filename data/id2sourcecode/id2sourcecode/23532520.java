    public void write(List<Dive> dives, LogBook logBook, File dest, InputStream xsdInputStream, DiveSiteManagerFacade diveSiteManagerFacade, LogBookManagerFacade logBookManagerFacade) throws IOException, XMLValidationException, DataStoreException {
        List<DiveSite> diveSites = LogBookUtilities.getDiveSites(dives, DiveSite.LOAD_MEDIUM, diveSiteManagerFacade);
        List<Diver> divers = LogBookUtilities.getDivers(dives, logBook.getOwner());
        List<Material> materials = LogBookUtilities.getMaterials(dives);
        String divesString = getLogBookXMLString(logBook, dives, diveSites, divers, materials, xsdInputStream);
        String diveSitesString = getDiveSitesXMLString(logBook, dives, diveSites, divers, xsdInputStream);
        String diversString = getDiversXMLString(logBook, dives, diveSites, divers, xsdInputStream);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
        ZipEntry ze = new ZipEntry("logbook.xml");
        zos.putNextEntry(ze);
        zos.write(divesString.getBytes("UTF-8"));
        zos.flush();
        ze = new ZipEntry("divers.xml");
        zos.putNextEntry(ze);
        zos.write(diversString.getBytes("UTF-8"));
        zos.flush();
        ze = new ZipEntry("divesites.xml");
        zos.putNextEntry(ze);
        zos.write(diveSitesString.getBytes("UTF-8"));
        zos.flush();
        writeDiveSiteDocuments(zos, diveSites, diveSiteManagerFacade);
        writeDiveDocuments(zos, dives, logBookManagerFacade);
        zos.close();
        LOGGER.info("Export done.");
    }
