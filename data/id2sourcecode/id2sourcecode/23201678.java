    @Override
    public void importArchive(Archive a) {
        Archive.Entry ae[] = a.entries();
        logger.debug("importArchive()");
        for (int i = 0; i < ae.length; i++) {
            logger.debug("archive[" + i + "].name()=" + ae[i].name());
            logger.debug("archive[" + i + "].digest()=" + ae[i].digest());
            logger.debug("archive[" + i + "].data().length=" + ae[i].data().length);
            this.putResource(ae[i].name(), a.getResourceAsByteArray(ae[i].name()));
        }
    }
