   * @return array of dayly data (missing value as Float.NaN)
   * @throws Exception
   */
    public static float[] getIndex(String element, int dayIdFrom, int dayIdTo) throws Exception {
        DateInterval dateInterval = new DateInterval(new WDCDay(dayIdFrom), new WDCDay(dayIdTo));
        return getIndex(element, dateInterval);
    }

    /**
   * Gets several days of data for specific element and location
   * @param element The element (field) to get data for
   * @param dateInterval The dateInterval
   * @return array of dayly data (missing value as Float.NaN)
   * @throws Exception
   */
    public static float[] getIndex(String element, DateInterval dateInterval) throws Exception {
        Connection con = null;
        Statement stmt = null;
        try {
            con = ConnectionPool.getConnection("Amie");
            stmt = con.createStatement();
            float[] data = getIndex(stmt, element, dateInterval.getDateFrom().getDayId(), dateInterval.getDateTo().getDayId());
            return data;
        } catch (Exception e) {
            throw new Exception("Data are not available: " + e.toString());
        } finally {
            try {
