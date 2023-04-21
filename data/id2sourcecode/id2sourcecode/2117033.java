    protected ISqlReadCursor<Data> prepareCursor() {
        int numberOfGapsToQualify = parameterService.getInt(ParameterConstants.ROUTING_MAX_GAPS_TO_QUALIFY_IN_SQL, 100);
        this.dataGaps = dataService.findDataGaps();
        String channelId = context.getChannel().getChannelId();
        String sql = qualifyUsingDataGaps(dataGaps, numberOfGapsToQualify, getSql(SELECT_DATA_USING_GAPS_SQL, context.getChannel().getChannel()));
        ISqlTemplate sqlTemplate = symmetricDialect.getPlatform().getSqlTemplate();
        int numberOfArgs = 1 + 2 * (numberOfGapsToQualify < dataGaps.size() ? numberOfGapsToQualify : dataGaps.size());
        Object[] args = new Object[numberOfArgs];
        int[] types = new int[numberOfArgs];
        args[0] = channelId;
        types[0] = Types.VARCHAR;
        for (int i = 0; i < numberOfGapsToQualify && i < dataGaps.size(); i++) {
            DataGap gap = dataGaps.get(i);
            args[i * 2 + 1] = gap.getStartId();
            types[i * 2 + 1] = Types.NUMERIC;
            if ((i + 1) == numberOfGapsToQualify && (i + 1) < dataGaps.size()) {
                args[i * 2 + 2] = dataGaps.get(dataGaps.size() - 1).getEndId();
            } else {
                args[i * 2 + 2] = gap.getEndId();
            }
            types[i * 2 + 2] = Types.NUMERIC;
        }
        this.currentGap = dataGaps.remove(0);
        return sqlTemplate.queryForCursor(sql, new ISqlRowMapper<Data>() {

            public Data mapRow(Row row) {
                return dataService.mapData(row);
            }
        }, args, types);
    }
