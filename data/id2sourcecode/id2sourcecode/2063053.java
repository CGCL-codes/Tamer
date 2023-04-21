    public static void main(String[] args) throws Exception {
        Connection rheaCon = null;
        OutputStream os = null;
        try {
            rheaCon = OracleDatabaseInstance.getInstance(args[0]).getConnection();
            os = new FileOutputStream(args[1]);
            IRheaReader rheaReader = new RheaDbReader(new RheaCompoundDbReader(rheaCon));
            Collection<Long> allPublicReactionIds = rheaReader.findAllPublic();
            Collection<Reaction> allPublicReactions = new HashSet<Reaction>();
            for (Long id : allPublicReactionIds) {
                Reaction r = rheaReader.findByReactionId(id);
                if (!r.isComplex() && !r.getDirection().equals(Direction.UN)) {
                    LOGGER.warn("Skipping reaction of defined direction RHEA:" + r.getId());
                    continue;
                }
                allPublicReactions.add(r);
                LOGGER.info("Added RHEA:" + r.getId().toString());
            }
            LOGGER.info("Writing OWL model...");
            Biopax.write(allPublicReactions, os, null);
            LOGGER.info("OWL model written!");
        } catch (IOException e) {
            LOGGER.error("Unable to read/write", e);
        } finally {
            if (rheaCon != null) rheaCon.close();
            if (os != null) os.close();
        }
    }
