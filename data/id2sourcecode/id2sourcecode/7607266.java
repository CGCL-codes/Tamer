    private void readTrainer() throws SQLException {
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        gregoriancalendar.setTime(m_clModel.getBasics().getDatum());
        gregoriancalendar.add(Calendar.WEEK_OF_YEAR, -WEEKS_BACK);
        Timestamp start = new Timestamp(gregoriancalendar.getTimeInMillis());
        int iLeadership = -1;
        int iLastLeadership = -1;
        int iID = -1;
        int iLastID = -1;
        ResultSet resultset = m_clJDBC.executeQuery("select SPIELERID, FUEHRUNG, DATUM from SPIELER " + "where TRAINERTYP <> -1 and DATUM <= '" + start + "' order by DATUM desc");
        try {
            boolean gotInitial = false;
            if (resultset.next()) {
                iLeadership = resultset.getInt("FUEHRUNG");
                iID = iLastID = resultset.getInt("SPIELERID");
                m_clPoints.add(new Point(resultset.getTimestamp("DATUM"), iLeadership, START_TRAINER_PT));
                gotInitial = true;
            }
            resultset = m_clJDBC.executeQuery("select SPIELERID, FUEHRUNG, DATUM from SPIELER " + "where TRAINERTYP <> -1 and DATUM > '" + start + "' and DATUM < '" + m_clModel.getBasics().getDatum() + "' order by DATUM");
            while (resultset.next()) {
                iLeadership = resultset.getInt("FUEHRUNG");
                iID = resultset.getInt("SPIELERID");
                if (!gotInitial) {
                    m_clPoints.add(new Point(resultset.getTimestamp("DATUM"), iLeadership, START_TRAINER_PT));
                    gotInitial = true;
                }
                if (iID != iLastID) {
                    m_clPoints.add(new Point(resultset.getTimestamp("DATUM"), iLeadership, NEW_TRAINER_PT));
                } else if (iLastLeadership != -1 && iLeadership != iLastLeadership) {
                    m_clPoints.add(new Point(resultset.getTimestamp("DATUM"), iLeadership, TRAINER_DOWN_PT));
                }
                iLastLeadership = iLeadership;
                iLastID = iID;
            }
        } catch (Exception e) {
            ErrorLog.writeln("Error reading trainer. Initial time: " + start);
            ErrorLog.write(e);
        }
    }
