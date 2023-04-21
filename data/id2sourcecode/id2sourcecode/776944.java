    public String[][] getChannelTable() {
        try {
            PreparedStatement pstmt;
            pstmt = con.prepareStatement("SELECT Count(user) as count,channel FROM userchans GROUP BY channel ORDER BY count DESC LIMIT 10;");
            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> a = new ArrayList<String>();
            ArrayList<String> b = new ArrayList<String>();
            while (rs.next()) {
                a.add(rs.getString("channel").toLowerCase());
                b.add(rs.getString("count"));
            }
            String[][] r = new String[a.size()][2];
            if (a.size() > 0) {
                for (int n = 0; n < r.length; n++) {
                    r[n][0] = a.get(n);
                    r[n][1] = b.get(n);
                }
                return r;
            } else {
                return new String[][] { { "0", "0" }, { "0", "0" } };
            }
        } catch (Exception e) {
            return new String[][] { { "0", "0" }, { "0", "0" } };
        }
    }
