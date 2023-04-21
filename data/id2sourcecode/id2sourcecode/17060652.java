    protected void parseLineups() throws DataDirectException {
        try {
            log.write(sdf.format(new java.util.Date()));
            log.write("\tParsing lineups top-level element");
            log.write(Parser.END_OF_LINE);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        PreparedStatement lineupStatement = null;
        PreparedStatement mapStatement = null;
        try {
            lineupStatement = connection.prepareStatement(RDBMSParser.LINEUP_STATEMENT);
            mapStatement = connection.prepareStatement(RDBMSParser.MAP_STATEMENT);
            reader.next();
            toStartTag();
            while (reader.getLocalName().equals("lineup")) {
                Lineup lineup = getLineup();
                lineupStatement.setString(1, lineup.getId());
                lineupStatement.setString(2, lineup.getName());
                lineupStatement.setString(3, lineup.getLocation());
                lineupStatement.setString(4, lineup.getType().toString());
                lineupStatement.setString(5, lineup.getDevice());
                lineupStatement.setString(6, lineup.getPostalCode());
                lineupStatement.addBatch();
                for (Iterator iterator = lineup.getMap().iterator(); iterator.hasNext(); ) {
                    Map map = (Map) iterator.next();
                    mapStatement.setString(1, lineup.getId());
                    mapStatement.setInt(2, map.getStation());
                    mapStatement.setString(3, map.getChannel());
                    mapStatement.setInt(4, map.getChannelMinor());
                    mapStatement.setDate(5, new java.sql.Date(map.getFrom().getDate().getTime()));
                    mapStatement.setDate(6, new java.sql.Date(map.getTo().getDate().getTime()));
                    mapStatement.addBatch();
                }
            }
            lineupStatement.executeBatch();
            mapStatement.executeBatch();
            connection.commit();
        } catch (XMLStreamException xsex) {
            throw new DataDirectException(xsex);
        } catch (SQLException sex) {
            throw new DataDirectException(sex);
        } finally {
            closeStatement(lineupStatement);
            closeStatement(mapStatement);
        }
    }
