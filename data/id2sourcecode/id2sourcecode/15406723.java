    private static final int getNextINR(Connection conn, String sql, int beginINR, int offset) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql + "INR>='" + FunctionMapper.str8(beginINR) + "' AND INR<?");
        int endINR = beginINR, minINR = beginINR, maxINR = 0, size = 0;
        ResultSet rs = null;
        while (Math.abs(size - offset) > 100) {
            if (size < offset) {
                minINR = endINR;
                if (maxINR == 0) endINR += +offset; else endINR = (minINR + maxINR) / 2;
            } else {
                maxINR = endINR;
                endINR = (minINR + maxINR) / 2;
            }
            stmt.setString(1, FunctionMapper.str8(endINR));
            rs = stmt.executeQuery();
            rs.next();
            size = rs.getInt(1);
            rs.close();
        }
        stmt.close();
        if (log.isDebugEnabled()) log.debug("size:" + size + "\tresult:" + endINR);
        return endINR;
    }
