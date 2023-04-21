    public String[] getChannelUsers(String chan) {
        try {
            PreparedStatement pstmt;
            pstmt = con.prepareStatement("SELECT * FROM userchans WHERE channel = ?");
            pstmt.setString(1, chan);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> a = new ArrayList<String>();
            while (rs.next()) {
                a.add(rs.getString("user"));
            }
            if (a.size() > 0) {
                String[] r = (String[]) a.toArray(new String[a.size()]);
                return r;
            } else {
                return new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
            }
        } catch (Exception e) {
            return new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" };
        }
    }
