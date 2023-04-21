    public int getChannelCount() {
        try {
            PreparedStatement pstmt;
            pstmt = con.prepareStatement("SELECT SQL_CALC_FOUND_ROWS * FROM userchans GROUP BY channel");
            ResultSet rs = pstmt.executeQuery();
            pstmt = con.prepareStatement("SELECT FOUND_ROWS();");
            rs = pstmt.executeQuery();
            rs.first();
            int channels = rs.getInt(1);
            rs.close();
            if (channels > maxChannels) {
                maxChannels = channels;
            }
            return channels;
        } catch (Exception e) {
            return 0;
        }
    }
