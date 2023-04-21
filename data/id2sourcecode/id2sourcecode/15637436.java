    public void save(String filename, InputStream in) {
        try {
            filename = java.net.URLDecoder.decode(filename, "UTF-8");
            String mimetype = getMimeType(m_req, filename);
            m_res.setContentType(mimetype);
            String agent = m_req.getHeader("USER-AGENT");
            String suffix = filename.substring(filename.indexOf(".") + 1, filename.length());
            m_res.setContentType(MimeType.getContentType(suffix));
            if (filename.indexOf("swf") != -1) {
                m_res.setContentType("application/x-shockwave-flash");
            } else {
                if (null != agent && -1 != agent.indexOf("MSIE")) {
                    String codedfilename = java.net.URLEncoder.encode(filename, "UTF-8");
                    codedfilename = StringUtils.replace(codedfilename, "+", "%20");
                    if (codedfilename.length() > 150) {
                        codedfilename = new String(filename.getBytes("GBK"), "ISO8859-1");
                        codedfilename = StringUtils.replace(codedfilename, " ", "%20");
                    }
                    m_res.setHeader("Content-Disposition", "attachment;filename=\"" + codedfilename + "\"");
                } else if (null != agent && -1 != agent.indexOf("Firefox")) {
                    String codedfilename = javax.mail.internet.MimeUtility.encodeText(filename, "UTF-8", "B");
                    m_res.setHeader("Content-Disposition", "attachment;filename=\"" + codedfilename + "\"");
                } else {
                    m_res.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
                }
            }
            if (out == null) out = m_res.getOutputStream();
            int read = 0;
            byte buf[] = new byte[4096];
            while ((read = in.read(buf, 0, 4096)) != -1) {
                out.write(buf, 0, read);
            }
            if (out != null) {
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("没有找到文件。");
        } catch (IOException e) {
            System.out.println("文件下载出错。");
        }
    }
