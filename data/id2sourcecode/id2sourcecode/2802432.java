    public String getChannel() {
        if (!this.hasResults) return "";
        String result = "";
        try {
            result = rs.getString("channel");
            this.hasResults = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
