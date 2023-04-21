    public void uploadHTMLandWML(MP_USER mpUser, MP_POST_CHANNEL mpChannel) throws java.sql.SQLException, java.io.IOException {
        Connection con = db.getConnection();
        log.log(Level.INFO, "uploading HTML+WML for channel " + mpUser.getUserId() + ":" + mpChannel.getChannelName());
        String query = "select " + "user_id, " + "channel_name, " + "post_email, " + "post_date, " + "title, " + "description, " + "image_mime_type " + "from blog_post " + "where " + " user_id = ? " + "AND " + " channel_name = ? " + " ORDER BY post_date DESC";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, mpUser.getUserId());
        stmt.setString(2, mpChannel.getChannelName());
        SimpleDateFormat udf = new SimpleDateFormat(mpChannel.getChannelDateFormat());
        ResultSet rs = stmt.executeQuery();
        StringBuffer index_xml = new StringBuffer();
        index_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        index_xml.append("<document>\n  \n  <properties>\n    <title>" + mpChannel.getChannelDisplayName() + "</title>\n  </properties>\n  \n  <body>\n\n");
        index_xml.append("<menu name=\"Main\">\n      <item name=\"Index\"       href=\"index\"/>\n      <item name=\"Archive\"     href=\"archive\"/>\n    </menu>\n");
        int row = 0;
        while (rs.next()) {
            if (row >= 15) break;
            MP_POST post = new MP_POST();
            post.setValues(rs);
            index_xml.append("<post><date>" + udf.format(post.getPostDate()) + "</date><title>" + post.getTitle() + "</title><img height=\"64\" width=\"64\" src=\"" + getPostFilename(post) + "\"></img><message>" + post.getDescription() + "</message></post>");
            row++;
        }
        index_xml.append("</body></document>\n");
        Calendar match_date = new GregorianCalendar();
        Calendar archive_date = null;
        Vector months = new Vector();
        rs.beforeFirst();
        while (rs.next()) {
            MP_POST post = new MP_POST();
            post.setValues(rs);
            match_date.setTime(post.getPostDate());
            if (archive_date == null) {
                archive_date = (Calendar) match_date.clone();
                months.add(archive_date.getTime());
            } else if (archive_date.get(Calendar.MONTH) != match_date.get(Calendar.MONTH)) {
                archive_date = (Calendar) match_date.clone();
                months.add(archive_date.getTime());
            }
        }
        log.log(Level.FINEST, "months=" + months);
        StringBuffer archive_menu_xml = new StringBuffer();
        archive_menu_xml.append("<menu name=\"Main\"><item name=\"Index\" href=\"index\"/><item name=\"Archive\"     href=\"archive\"/></menu> <menu name=\"Sub\">");
        for (Iterator it = months.iterator(); it.hasNext(); ) {
            java.util.Date dd = (java.util.Date) it.next();
            archive_menu_xml.append("<item name=\"" + archive_dateFormat.format(dd) + "\" href=\"archive_" + archive_file_dateFormat.format(dd) + "\"/>");
        }
        archive_menu_xml.append("</menu>");
        Vector archive_xmls = new Vector();
        Vector archive_dates = new Vector();
        StringBuffer archive_xml = null;
        rs.beforeFirst();
        while (rs.next()) {
            MP_POST post = new MP_POST();
            post.setValues(rs);
            match_date.setTime(post.getPostDate());
            if (archive_xml == null) {
                archive_xml = new StringBuffer();
                archive_date.setTime(post.getPostDate());
                archive_xmls.add(archive_xml);
                archive_dates.add(archive_date.clone());
                archive_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                archive_xml.append("<document>\n  \n  <properties>\n    <title>" + mpChannel.getChannelDisplayName() + "</title>\n  </properties>\n  \n  <body>\n\n");
                archive_xml.append(archive_menu_xml);
            } else {
                if (archive_date.get(Calendar.MONTH) != match_date.get(Calendar.MONTH)) {
                    archive_xml.append("</body></document>\n");
                    archive_xml = new StringBuffer();
                    archive_date.setTime(post.getPostDate());
                    archive_xmls.add(archive_xml);
                    archive_dates.add(archive_date.clone());
                    archive_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    archive_xml.append("<document>\n  \n  <properties>\n    <title>" + mpChannel.getChannelDisplayName() + "</title>\n  </properties>\n  \n  <body>\n\n");
                    archive_xml.append(archive_menu_xml);
                }
            }
            archive_xml.append("<archive-post><date>" + udf.format(post.getPostDate()) + "</date><title>" + post.getTitle() + "</title><img height=\"64\" width=\"64\" src=\"" + getPostFilename(post) + "\"></img><message>" + post.getDescription() + "</message></archive-post>");
        }
        if (archive_xml != null) {
            archive_xml.append("</body></document>\n");
        }
        archive_xml = new StringBuffer();
        archive_xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        archive_xml.append("<document>\n  \n  <properties>\n    <title>" + mpChannel.getChannelDisplayName() + "</title>\n  </properties>\n  \n  <body>\n\n");
        archive_xml.append(archive_menu_xml);
        archive_xml.append("</body></document>\n");
        stmt.close();
        log.log(Level.FINEST, "archive_menu_xml=" + archive_menu_xml);
        log.log(Level.FINEST, "index_xml=" + index_xml);
        log.log(Level.FINEST, "archive_xml=" + archive_xml);
        log.log(Level.FINEST, "archive_xmls=" + archive_xmls);
        Utils.ftpUploadFile(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "style.css", webPath + "/style.css");
        Utils.ftpUploadFile(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "trmenu.gif", webPath + "/trmenu.gif");
        String index_html = htmlGenerator.generate("html.vsl", index_xml.toString());
        log.log(Level.FINEST, "index_html=" + index_html);
        Utils.ftpUploadTxt(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "index.html", index_html);
        String index_wml = wmlGenerator.generate("wml.vsl", index_xml.toString());
        log.log(Level.FINEST, "index_wml=" + index_wml);
        Utils.ftpUploadTxt(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "index.wml", index_wml);
        String archive_html = htmlGenerator.generate("html.vsl", archive_xml.toString());
        log.log(Level.FINEST, "archive_html=" + archive_html);
        Utils.ftpUploadTxt(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "archive.html", archive_html);
        String archive_wml = wmlGenerator.generate("wml.vsl", archive_xml.toString());
        log.log(Level.FINEST, "archive_wml=" + archive_wml);
        Utils.ftpUploadTxt(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "archive.wml", archive_wml);
        int di = 0;
        for (Iterator it = archive_xmls.iterator(); it.hasNext(); di++) {
            String a_xml = ((StringBuffer) it.next()).toString();
            java.util.Date dd = (java.util.Date) months.get(di);
            String a_html = htmlGenerator.generate("html.vsl", a_xml.toString());
            log.log(Level.FINEST, "a_html=" + a_html);
            Utils.ftpUploadTxt(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "archive_" + archive_file_dateFormat.format(dd) + ".html", a_html);
            String a_wml = wmlGenerator.generate("wml.vsl", a_xml.toString());
            log.log(Level.FINEST, "a_wml=" + a_wml);
            Utils.ftpUploadTxt(mpChannel.getFtpHost(), mpChannel.getFtpUser(), mpChannel.getFtpPasswd(), mpChannel.getFtpPath(), "archive_" + archive_file_dateFormat.format(dd) + ".wml", a_wml);
        }
        log.log(Level.INFO, "HTML+WML uploaded!");
    }
