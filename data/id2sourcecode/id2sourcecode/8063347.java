    public void add(DocumentRecycle doc, DocResource[] allResources) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DocumentRecycle tmpDoc = DocumentRecycle.getInstance(doc.getId(), (Timestamp) doc.getBakDate());
        if (tmpDoc != null) {
            tmpDoc.delete(doc.getBakor());
        }
        try {
            String sql = "insert into t_ip_doc_bak(id,acty_inst_id,doctype_path,channel_path,content_file,attach_status,year_no,periodical_no,word_no,title,title_color,sub_title,author,emit_date,emit_unit,editor_remark,keywords,pertinent_words,abstract_words,source_id,security_level_id,creater,create_date,lastest_modify_date,remark_prop,notes,workflow_id,reservation1,reservation2,reservation3,reservation4,reservation5,reservation6,bakor,bak_date,bak_flag,hyperlink)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, doc.getId());
            preparedStatement.setLong(2, Long.parseLong(doc.getActyInstId()));
            preparedStatement.setString(3, doc.getDoctypePath());
            preparedStatement.setString(4, doc.getChannelPath());
            preparedStatement.setString(5, doc.getContentFile());
            preparedStatement.setString(6, doc.getAttachStatus());
            preparedStatement.setInt(7, doc.getYearNo());
            preparedStatement.setInt(8, doc.getPeriodicalNo());
            preparedStatement.setInt(9, doc.getWordNo());
            preparedStatement.setString(10, doc.getTitle());
            preparedStatement.setString(11, doc.getTitleColor());
            preparedStatement.setString(12, doc.getSubTitle());
            preparedStatement.setString(13, doc.getAuthor());
            preparedStatement.setTimestamp(14, (Timestamp) doc.getEmitDate());
            preparedStatement.setString(15, doc.getEmitUnit());
            preparedStatement.setString(16, doc.getEditorRemark());
            preparedStatement.setString(17, doc.getKeywords());
            preparedStatement.setString(18, doc.getPertinentWords());
            preparedStatement.setString(19, doc.getAbstractWords());
            preparedStatement.setInt(20, doc.getSourceId());
            preparedStatement.setInt(21, doc.getSecurityLevelId());
            preparedStatement.setInt(22, doc.getCreater());
            preparedStatement.setTimestamp(23, (Timestamp) doc.getCreateDate());
            preparedStatement.setTimestamp(24, (Timestamp) doc.getLastestModifyDate());
            preparedStatement.setString(25, String.valueOf(doc.getRemarkProp()));
            preparedStatement.setString(26, doc.getNotes());
            preparedStatement.setInt(27, 0);
            preparedStatement.setString(28, doc.getReservation1());
            preparedStatement.setString(29, doc.getReservation2());
            preparedStatement.setString(30, doc.getReservation3());
            preparedStatement.setString(31, doc.getReservation4());
            preparedStatement.setString(32, doc.getReservation5());
            preparedStatement.setString(33, doc.getReservation6());
            preparedStatement.setInt(34, doc.getBakor());
            preparedStatement.setTimestamp(35, (Timestamp) doc.getBakDate());
            preparedStatement.setString(36, doc.isBakFlag() ? "1" : "0");
            preparedStatement.setString(37, doc.getHyperlink());
            preparedStatement.executeUpdate();
            for (int i = 0; i < allResources.length; i++) {
                new DocResourceRecycle(allResources[i], doc.getBakDate()).add();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("���ĵ��ŵ�����վʧ��,�ĵ�id=" + doc.getId(), ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
    }
