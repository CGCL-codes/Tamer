    private void generateRasterLayers(Connection con, Project project, Group group, Element element) throws Exception {
        String query = null;
        StringBuffer parent_group = null;
        String group_code = "";
        if (group != null && new Boolean(group.getPeriodical()).booleanValue()) {
            if (group != null && group.getCode().contains("months")) {
                parent_group = new StringBuffer();
                parent_group.append("%" + group.getParent().trim() + "%");
            } else {
                parent_group = new StringBuffer();
                parent_group.append("%%");
            }
            query = "select Raster_ID, Raster_Name, Raster_Path, " + "Raster_Visibility, Raster_TimePeriod, " + "Raster_CLR, Raster_Group_Parent, " + "Layer_Type_Code, Raster_LastUpdated, " + "Raster_Resource, CLR_Resource, Raster_TimeCode " + "from rasterlayer where Proj_ID = ? and Group_Code = ? " + "and raster_timeperiod > " + "(SELECT DATE_SUB(max(Raster_TimePeriod), " + "INTERVAL 2 MONTH) from rasterlayer) " + "and LTRIM(Raster_Group_Parent) like ? " + "order by raster_timeperiod desc, Raster_Name, " + "raster_order";
        } else {
            query = "select Raster_ID, Raster_Name, Raster_Path, " + "Raster_Visibility, Raster_TimePeriod, " + "Raster_CLR, Raster_Group_Parent, " + "Layer_Type_Code, Raster_LastUpdated, " + "Raster_Resource, CLR_Resource, Raster_TimeCode " + "from rasterlayer where Proj_ID = ? and Group_Code = ? " + "order by raster_timeperiod desc,Raster_Name,raster_order";
        }
        if (group != null) {
            group_code = group.getCode();
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        long raster_id = -1;
        String raster_name = null;
        String raster_lastUpdated = null;
        String raster_path = null;
        String layer_type = null;
        String raster_visibility = null;
        String time_period = null;
        String raster_clr = null;
        String group_parent = null;
        String raster_resource = null;
        String clr_resource = null;
        String time_code = null;
        int project_id = project.getProjId().intValue();
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, project_id);
            pstmt.setString(2, group_code);
            if (parent_group != null) {
                pstmt.setString(3, parent_group.toString());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                raster_id = rs.getLong(1);
                raster_name = rs.getString(2);
                raster_path = rs.getString(3);
                raster_visibility = rs.getString(4);
                time_period = rs.getString(5);
                if (rs.getString(6) != null) {
                    raster_clr = rs.getString(6).trim();
                } else {
                    raster_clr = rs.getString(6);
                }
                if (rs.getString(7) != null) {
                    group_parent = rs.getString(7).trim();
                } else {
                    group_parent = rs.getString(7);
                }
                if (rs.getString(8) != null) {
                    layer_type = rs.getString(8).trim();
                } else {
                    layer_type = "0";
                }
                if (rs.getString(9) != null) {
                    raster_lastUpdated = rs.getString(9);
                } else {
                    Date today_date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    raster_lastUpdated = formatter.format(today_date);
                }
                raster_resource = rs.getString(10);
                clr_resource = rs.getString(11);
                time_code = rs.getString(12);
                Rasterlayer raster_layer = new Rasterlayer();
                raster_layer.setRasterID(raster_id);
                raster_layer.setRasterName(raster_name);
                raster_layer.setRasterPath(raster_path);
                raster_layer.setRasterVisibility(raster_visibility);
                raster_layer.setRasterCLR(raster_clr);
                raster_layer.setRasterGroupParent(group_parent);
                raster_layer.setLayerTypeCode(layer_type);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date dtime_period = formatter.parse(time_period);
                raster_layer.setRasterTimePeriod(dtime_period);
                Date last_updated = formatter.parse(raster_lastUpdated);
                raster_layer.setRasterLastUpdated(last_updated);
                raster_layer.setRasterResource(raster_resource);
                raster_layer.setCLRResource(clr_resource);
                long date_difference = (today.getTime() - last_updated.getTime()) / (1000L * 60L * 60L * 24L);
                if (date_difference < 15 && !rl_upd) {
                    generation_text.append("<br>" + RL_UPDATES);
                    rl_upd = true;
                }
                generateRasterLayerElement(con, element, raster_layer, project);
                if ((group_code.equals(ESTIMATED_RAINFALL) || group_code.equals(VEGETATION_INDEX)) && (!raster_name.contains("DA") && !raster_name.contains("DP") && !raster_name.contains("DY"))) {
                    PreparedStatement apstmt = null;
                    ResultSet ars = null;
                    String sql = "select Raster_ID, Raster_Name, Raster_Path, " + "Raster_Visibility, Raster_TimePeriod, " + "Raster_CLR, Raster_Group_Parent, " + "Layer_Type_Code, Raster_LastUpdated, " + "Raster_Resource, CLR_Resource, " + "Raster_TimeCode " + "from rasterlayer where Proj_ID = ? and " + "Group_Code = ? and Raster_Name like " + "'%Average%' and " + "MONTH(raster_timeperiod) = ? and " + "DAYOFMONTH(raster_timeperiod) = ? and " + "Raster_TimeCode = ? " + "order by raster_timeperiod desc";
                    int month = Integer.parseInt(time_period.substring(5, 7));
                    int day = Integer.parseInt(time_period.substring(8, 10));
                    apstmt = con.prepareStatement(sql);
                    apstmt.setInt(1, project_id);
                    apstmt.setString(2, group_code);
                    apstmt.setInt(3, month);
                    apstmt.setInt(4, day);
                    apstmt.setString(5, time_code);
                    ars = apstmt.executeQuery();
                    if (ars.next()) {
                        raster_id = ars.getLong(1);
                        raster_name = ars.getString(2);
                        raster_path = ars.getString(3);
                        raster_visibility = ars.getString(4);
                        time_period = ars.getString(5);
                        if (ars.getString(6) != null) {
                            raster_clr = ars.getString(6).trim();
                        } else {
                            raster_clr = ars.getString(6);
                        }
                        if (ars.getString(7) != null) {
                            group_parent = ars.getString(7).trim();
                        } else {
                            group_parent = ars.getString(7);
                        }
                        if (ars.getString(8) != null) {
                            layer_type = ars.getString(8).trim();
                        } else {
                            layer_type = "0";
                        }
                        raster_lastUpdated = ars.getString(9);
                        raster_resource = ars.getString(10);
                        clr_resource = ars.getString(11);
                        time_code = ars.getString(12);
                        Rasterlayer avg_raster_layer = new Rasterlayer();
                        avg_raster_layer.setRasterID(raster_id);
                        avg_raster_layer.setRasterName(raster_name);
                        avg_raster_layer.setRasterPath(raster_path);
                        avg_raster_layer.setRasterVisibility(raster_visibility);
                        avg_raster_layer.setRasterCLR(raster_clr);
                        avg_raster_layer.setRasterGroupParent(group_parent);
                        avg_raster_layer.setLayerTypeCode(layer_type);
                        Date avg_dtime_period = formatter.parse(time_period);
                        avg_raster_layer.setRasterTimePeriod(avg_dtime_period);
                        Date avg_last_updated = formatter.parse(raster_lastUpdated);
                        avg_raster_layer.setRasterLastUpdated(avg_last_updated);
                        avg_raster_layer.setRasterResource(raster_resource);
                        avg_raster_layer.setCLRResource(clr_resource);
                        generateRasterLayerElement(con, element, avg_raster_layer, project);
                    }
                }
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            String sql_exc = "ProjectGenerator.generateRasterLayers - " + " SQLException: ";
            throw new Exception(sql_exc + e);
        }
    }
