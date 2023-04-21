    public ArrayList<PropertyInfo> getChannelList() {
        String sql = String.format("select DISTINCT s.ID as Id, s.sectionname as Name From Section s, SpotLight t where t.sectionid = s.ID and t.leadflag=1");
        ResultSet rs = dbh.executeQuery(sql);
        ArrayList<PropertyInfo> list = new ArrayList<PropertyInfo>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    PropertyInfo Info = new PropertyInfo();
                    Info.setId(rs.getLong("Id"));
                    Info.setName(transform(rs.getString("Name")));
                    list.add(Info);
                    Info = null;
                }
                rs.close();
                return list;
            } catch (Exception e) {
                printStackTrace(e);
                return list;
            }
        }
        return list;
    }
