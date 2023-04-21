    @SuppressWarnings("deprecation")
    public static void insertPic(Connection conn, DBPicInfo picInfo, int lock, InputStream fin) throws SQLException, IOException, JpegProcessingException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        OutputStream out = null;
        BLOB blob = null;
        byte[] buf = null;
        long uploadTime = 0;
        try {
            long beginTime = new Date().getTime();
            String sql = "insert into DBPic " + "values (bnr_seq.nextval,?,?,EMPTY_BLOB())";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, picInfo.getName());
            pstmt.setInt(2, picInfo.getRate());
            pstmt.executeUpdate();
            pstmt.close();
            sql = "select bnr_seq.currval from dual";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);
            rs.next();
            int bnr = rs.getInt(1);
            pstmt.close();
            rs.close();
            sql = "select bild from DBPic where bnr=? for update";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bnr);
            rs = pstmt.executeQuery();
            rs.next();
            blob = (BLOB) rs.getBlob("bild");
            out = blob.getBinaryOutputStream();
            buf = new byte[fin.available()];
            fin.read(buf);
            out.write(buf);
            out.flush();
            long endTime = new Date().getTime();
            uploadTime = endTime - beginTime;
            StatisticApp.query("upload", "sql", buf.length, uploadTime, 1);
            if (picInfo.getCategories() != null) insertPicInCategories(conn, bnr, picInfo.getCategories());
            if (picInfo.getKeywords() != null) insertPicInKeywords(conn, bnr, picInfo.getKeywords());
            insertExif(conn, bnr, buf);
            insertThumb(conn, bnr, lock, buf);
            fin.close();
            out.close();
            rs.close();
            pstmt.close();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } catch (FileNotFoundException e) {
            conn.rollback();
            throw e;
        } catch (IOException e) {
            conn.rollback();
            throw e;
        } catch (MetadataException e) {
            conn.rollback();
        } finally {
            fin.close();
            out.close();
            rs.close();
            pstmt.close();
        }
    }
