    public void add(DocumentPublish doc) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            String sql = "insert into t_ip_browse(doc_id,channel_path," + "doctype_path,publisher,publish_date,order_no," + "valid_startdate,valid_enddate,syn_status," + "content_file,attach_status,year_no," + "periodical_no,word_no,title,title_color," + "sub_title,author,emit_date,emit_unit," + "editor_remark,keywords,pertinent_words," + "abstract_words,source_id,security_level_id," + "creater,create_date,lastest_modify_date,remark_prop," + "notes,reservation1,reservation2,reservation3," + "reservation4,reservation5,reservation6,hyperlink)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, doc.getId());
            preparedStatement.setString(2, doc.getChannelPath());
            preparedStatement.setString(3, doc.getDoctypePath());
            preparedStatement.setInt(4, doc.getPublisher());
            preparedStatement.setTimestamp(5, (Timestamp) doc.getPublishDate());
            preparedStatement.setInt(6, doc.getOrderNo());
            preparedStatement.setTimestamp(7, (Timestamp) doc.getValidStartDate());
            preparedStatement.setTimestamp(8, (Timestamp) doc.getValidEndDate());
            preparedStatement.setString(9, doc.isSynStatus() ? "1" : "0");
            preparedStatement.setString(10, doc.getContentFile());
            preparedStatement.setString(11, doc.getAttachStatus());
            preparedStatement.setInt(12, doc.getYearNo());
            preparedStatement.setInt(13, doc.getPeriodicalNo());
            preparedStatement.setInt(14, doc.getWordNo());
            preparedStatement.setString(15, doc.getTitle());
            preparedStatement.setString(16, doc.getTitleColor());
            preparedStatement.setString(17, doc.getSubTitle());
            preparedStatement.setString(18, doc.getAuthor());
            preparedStatement.setTimestamp(19, (Timestamp) doc.getEmitDate());
            preparedStatement.setString(20, doc.getEmitUnit());
            preparedStatement.setString(21, doc.getEditorRemark());
            preparedStatement.setString(22, doc.getKeywords());
            preparedStatement.setString(23, doc.getPertinentWords());
            preparedStatement.setString(24, doc.getAbstractWords());
            preparedStatement.setString(25, String.valueOf(doc.getSourceId()));
            preparedStatement.setInt(26, doc.getSecurityLevelId());
            preparedStatement.setInt(27, doc.getCreater());
            preparedStatement.setTimestamp(28, (Timestamp) doc.getCreateDate());
            if (doc.getLastestModifyDate() == null) {
                preparedStatement.setTimestamp(29, Function.getSysTime());
            } else {
                preparedStatement.setTimestamp(29, (Timestamp) doc.getLastestModifyDate());
            }
            preparedStatement.setString(30, String.valueOf(doc.getRemarkProp()));
            preparedStatement.setString(31, doc.getNotes());
            preparedStatement.setString(32, doc.getReservation1());
            preparedStatement.setString(33, doc.getReservation2());
            preparedStatement.setString(34, doc.getReservation3());
            preparedStatement.setString(35, doc.getReservation4());
            preparedStatement.setString(36, doc.getReservation5());
            preparedStatement.setString(37, doc.getReservation6());
            preparedStatement.setString(38, doc.getHyperlink());
            preparedStatement.executeUpdate();
            result = true;
        } catch (Exception ex) {
            result = false;
            log.error("����һƪ�����ĵ�ʧ�ܣ�", ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        try {
            String noticeChannelPath = Config_new.getInstance().getpath("NOTICE_CHANNEL_PATH");
            if (result && noticeChannelPath.equals(doc.getChannelPath())) {
                String yqsshouye = Config_new.getInstance().getpath("YQS_SHOU_YE_JSP");
                String strUserId = doc.getReservation4() == null ? "" : doc.getReservation4();
                String[] userIds = strUserId.split(",");
                if ("".equals(strUserId)) {
                    int publisher = doc.getPublisher();
                    User user = new User(publisher);
                    user.load();
                    Vector vv = user.getAllUsers();
                    Vector tmp = new Vector();
                    for (int i = 0; i < vv.size(); i++) {
                        User u = (User) vv.elementAt(i);
                        if (publisher == u.getUserID()) {
                            continue;
                        }
                        tmp.add(String.valueOf(u.getUserID()));
                    }
                    String[] tmpUserIds = new String[tmp.size()];
                    tmp.toArray(tmpUserIds);
                    userIds = tmpUserIds;
                }
                for (int i = 0; i < userIds.length; i++) {
                    if (!"".equals(userIds[i])) {
                        BusinessInterface.sendSystemMessage(Integer.parseInt(userIds[i]), "֪ͨͨ�棺" + doc.getTitle(), yqsshouye);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("wotalk����ϵͳ��Ϣ����", ex);
        }
    }
