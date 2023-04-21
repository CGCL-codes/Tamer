    @SuppressWarnings("unchecked")
    public void checkAfterUpdate(boolean checkData, HhEndDate from, HhEndDate to) throws ProgrammerException, UserException {
        Hiber.flush();
        Date supplyStartDate = getGenerationFirst().getStartDate().getDate();
        Date supplyFinishDate = getGenerationLast().getFinishDate() == null ? null : getGenerationLast().getFinishDate().getDate();
        if (checkData) {
            if (from.getDate().before(supplyStartDate) && ((Long) Hiber.session().createQuery("select count(*) from HhDatum datum where datum.channel.supply  = :supply and datum.endDate.date < :date").setEntity("supply", this).setTimestamp("date", supplyStartDate).uniqueResult()) > 0) {
                throw UserException.newInvalidParameter("There are half-hourly data before the start of the updated supply.");
            }
            if (supplyFinishDate != null && ((Long) Hiber.session().createQuery("select count(*) from HhDatum datum where datum.channel.supply  = :supply and datum.endDate.date > :date").setEntity("supply", this).setTimestamp("date", supplyFinishDate).uniqueResult()) > 0) {
                throw UserException.newInvalidParameter("There are half-hourly data after the end of the updated supply.");
            }
            Query query = Hiber.session().createQuery("select count(*) from HhDatum datum where datum.channel  = :channel and datum.endDate.date >= :startDate and datum.endDate.date <= :finishDate");
            for (SupplyGeneration generation : getGenerations(from, to)) {
                for (Channel channel : getChannels()) {
                    if (generation.getDceService(channel.getIsImport(), channel.getIsKwh()) == null) {
                        HhEndDate generationFinishDate = generation.getFinishDate();
                        if (generationFinishDate == null) {
                            if (((Long) Hiber.session().createQuery("select count(*) from HhDatum datum where datum.channel  = :channel and datum.endDate.date >= :startDate").setEntity("channel", channel).setTimestamp("startDate", generation.getStartDate().getDate()).uniqueResult()) > 0) {
                                throw UserException.newInvalidParameter("There are half-hourly data in " + channel + " and generation " + generation + " without a contract with the updated supply.");
                            }
                        } else {
                            if (((Long) query.setEntity("channel", channel).setTimestamp("startDate", generation.getStartDate().getDate()).setTimestamp("finishDate", generation.getFinishDate().getDate()).uniqueResult()) > 0) {
                                throw UserException.newInvalidParameter("There are half-hourly data without a contract, associated with the channel " + channel.getId() + " and supply generation '" + generation.getId() + "' .");
                            }
                        }
                    }
                }
            }
            if (from.getDate().before(supplyStartDate)) {
                for (SnagChannel snag : (List<SnagChannel>) Hiber.session().createQuery("from SnagChannel snag where snag.channel.supply  = :supply and snag.finishDate.date >= :startDate and snag.startDate.date <= :finishDate").setEntity("supply", this).setTimestamp("startDate", from.getDate()).setTimestamp("finishDate", new HhEndDate(supplyStartDate).getPrevious().getDate()).list()) {
                    snag.resolve(false);
                }
            }
            if (supplyFinishDate != null) {
                for (SnagChannel snag : (List<SnagChannel>) Hiber.session().createQuery("from SnagChannel snag where snag.channel.supply  = :supply and snag.finishDate.date >= :startDate").setEntity("supply", this).setTimestamp("startDate", from.getDate()).list()) {
                    snag.resolve(false);
                }
            }
            for (SupplyGeneration generation : getGenerations(from, to)) {
                for (Channel channel : getChannels()) {
                    DceService contractDce = generation.getDceService(channel.getIsImport(), channel.getIsKwh());
                    HhEndDate generationFinishDate = generation.getFinishDate();
                    for (SnagChannel snag : (List<SnagChannel>) (generationFinishDate == null ? Hiber.session().createQuery("from SnagChannel snag where snag.channel  = :channel and snag.finishDate.date >= :startDate").setEntity("channel", channel).setTimestamp("startDate", generation.getStartDate().getDate()).list() : Hiber.session().createQuery("from SnagChannel snag where snag.channel  = :channel and snag.finishDate.date >= :startDate and snag.startDate.date <= :finishDate").setEntity("channel", channel).setTimestamp("startDate", generation.getStartDate().getDate()).setTimestamp("finishDate", generation.getFinishDate().getDate()).list())) {
                        if (!snag.getService().equals(contractDce)) {
                            snag.resolve(false);
                        }
                    }
                }
            }
            checkForMissing(from, to);
        }
        if (from.getDate().before(supplyStartDate) && ((Long) Hiber.session().createQuery("select count(*) from RegisterRead read where read.mpan.supplyGeneration.supply  = :supply and read.presentDate.date < :date").setEntity("supply", this).setTimestamp("date", supplyStartDate).uniqueResult()) > 0) {
            throw UserException.newInvalidParameter("There are register reads before the start of the updated supply.");
        }
        if (supplyFinishDate != null && ((Long) Hiber.session().createQuery("select count(*) from RegisterRead read where read.mpan.supplyGeneration.supply  = :supply and read.presentDate.date > :date").setEntity("supply", this).setTimestamp("date", supplyFinishDate).uniqueResult()) > 0) {
            throw UserException.newInvalidParameter("There are register reads after the end of the updated supply.");
        }
        for (SupplyGeneration generation : getGenerations(from, to)) {
            for (RegisterRead read : (List<RegisterRead>) Hiber.session().createQuery("from RegisterRead read where read.mpan.supplyGeneration = :supplyGeneration").setEntity("supplyGeneration", generation).list()) {
                if (read.getPresentDate().getDate().before(generation.getStartDate().getDate()) || (generation.getFinishDate() != null && read.getPresentDate().getDate().after(generation.getFinishDate().getDate()))) {
                    SupplyGeneration targetGeneration = getGeneration(read.getPresentDate());
                    Mpan targetMpan = read.getMpan().getMpanTop().getLineLossFactor().getIsImport() ? targetGeneration.getImportMpan() : targetGeneration.getExportMpan();
                    if (targetMpan == null) {
                        throw UserException.newInvalidParameter("There's no MPAN for the meter read to move to.");
                    }
                    read.setMpan(targetMpan);
                }
            }
        }
        List<Meter> metersToRemove = new ArrayList<Meter>();
        for (Meter meter : meters) {
            if ((Long) Hiber.session().createQuery("select count(*) from SupplyGeneration generation where generation.meter = :meter").setEntity("meter", meter).uniqueResult() == 0) {
                metersToRemove.add(meter);
            }
        }
        for (Meter meterToRemove : metersToRemove) {
            meters.remove(meterToRemove);
        }
    }
