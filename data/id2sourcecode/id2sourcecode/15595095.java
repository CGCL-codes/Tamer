    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            setId(KeyGen.nextID(""));
            sql = "select * from t_article_category where category_id = ?";
            conn.prepare(sql);
            conn.setString(1, getCategoryId());
            DbRs rs = conn.executeQuery();
            if (rs == null || rs.size() == 0) {
                setErr("ѡ��ķ��಻����!");
                return;
            }
            String is_leaf = get(rs, 0, "is_leaf");
            if (!is_leaf.trim().equals("1")) {
                setErr("ѡ��ķ��಻��δ������!");
                return;
            }
            conn.setAutoCommit(false);
            sql = "insert into t_article_info (" + "article_id,article_title,author_id,author_name,article_content," + "category_id,issue_time,hit,doc_info" + ") values (?,?,?,?,?,?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.setString(2, getArticleTitle());
            conn.setString(3, getAuthorId());
            conn.setString(4, getAuthorName());
            conn.setString(5, getArticleContent());
            conn.setString(6, getCategoryId());
            conn.setString(7, TSSDate.fullTime());
            conn.setInt(8, 0);
            conn.setString(9, getDocInfo());
            conn.executeUpdate();
            sql = "update userinfo set article_num = article_num + 1 where user_id = ?";
            conn.prepare(sql);
            conn.setString(1, getAuthorId());
            conn.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                ex.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
