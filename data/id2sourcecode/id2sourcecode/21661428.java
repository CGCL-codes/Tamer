    public void oldUploadHTMLandWML(MP_USER mpUser, MP_POST_CHANNEL mpChannel) throws java.sql.SQLException, java.io.IOException {
        Connection con = db.getConnection();
        log.log(Level.INFO, "uploading HTML+WML for channel " + mpUser.getUserId() + ":" + mpChannel.getChannelName());
        String query = "select " + "user_id, " + "channel_name, " + "post_email, " + "post_date, " + "title, " + "description, " + "image_mime_type " + "from blog_post " + "where " + " user_id = ? " + "AND " + " channel_name = ? " + " ORDER BY post_date DESC";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, mpUser.getUserId());
        stmt.setString(2, mpChannel.getChannelName());
        FtpClient httpClient = new FtpClient();
        log.log(Level.INFO, "opening FTP to " + mpChannel.getFtpHost() + " for HTML: " + mpChannel.getFtpPath() + "/index.html");
        httpClient.openServer(mpChannel.getFtpHost());
        httpClient.login(mpChannel.getFtpUser(), mpChannel.getFtpPasswd());
        httpClient.binary();
        httpClient.cd(mpChannel.getFtpPath());
        FtpClient wmlClient = new FtpClient();
        log.log(Level.INFO, "opening FTP to " + mpChannel.getFtpHost() + " for WML: " + mpChannel.getFtpPath() + "/index.wml");
        wmlClient.openServer(mpChannel.getFtpHost());
        wmlClient.login(mpChannel.getFtpUser(), mpChannel.getFtpPasswd());
        wmlClient.binary();
        wmlClient.cd(mpChannel.getFtpPath());
        PrintWriter html = new PrintWriter(httpClient.put("index.html"));
        html.println("<html>");
        html.println("  <head>");
        html.println("    <title>" + mpUser.getFullName() + " Blog</title>");
        html.println("  </head>");
        html.println("  <body>");
        html.println("    <h1>" + mpUser.getFullName() + " Blog</h1>");
        PrintWriter wml = new PrintWriter(wmlClient.put("index.wml"));
        wml.println("<?xml version=\"1.0\"?>");
        wml.println("<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\">");
        wml.println("<wml>");
        wml.println(" <card id=\"index\" title=\"" + mpUser.getFullName() + " Blog\">");
        wml.println("   <p>");
        SimpleDateFormat udf = new SimpleDateFormat(mpChannel.getChannelDateFormat());
        ResultSet rs = stmt.executeQuery();
        int row = 0;
        while (rs.next()) {
            if (row >= 15) break;
            MP_POST post = new MP_POST();
            post.setValues(rs);
            html.println("    <b>" + post.getTitle() + "</b><br/>");
            html.println("    <i>" + udf.format(post.getPostDate()) + "</i><br/>");
            if (post.getImageMimeType() != null) {
                html.println("    <a href=\"" + getPostFilename(post) + "\"><img width=\"160\" border=\"0\" src=\"" + getPostFilename(post) + "\"></img></a><br/>");
            }
            html.println("    <span>" + post.getDescription() + "</span><br/>");
            html.println("<br/>");
            wml.println("    <b>" + post.getTitle() + "</b><br/>");
            wml.println("    <i>" + udf.format(post.getPostDate()) + "</i><br/>");
            if (post.getImageMimeType() != null) {
                wml.println("    <a href=\"" + getPostFilename(post) + "\"><img width=\"120\" border=\"0\" src=\"" + getPostFilename(post) + "\"></img></a><br/>");
            }
            wml.println("    " + post.getDescription() + "<br/><br/>");
            row++;
        }
        stmt.close();
        html.println("  </body>");
        html.println("</html>");
        html.flush();
        html.close();
        wml.println("   </p>");
        wml.println(" </card>");
        wml.println("</wml>");
        wml.flush();
        wml.close();
        log.log(Level.INFO, "HTML+WML uploaded!");
    }
