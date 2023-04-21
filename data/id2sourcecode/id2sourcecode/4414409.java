    public int doEndTag() throws JspException {
        HttpSession session = pageContext.getSession();
        try {
            IntactUserI user = uk.ac.ebi.intact.application.hierarchview.business.IntactUser.getCurrentInstance(session);
            String urlStr = user.getSourceURL();
            if (urlStr == null) {
                return EVAL_PAGE;
            }
            URL url;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException me) {
                String decodedUrl = URLDecoder.decode(urlStr, "UTF-8");
                pageContext.getOut().write("The source is malformed : <a href=\"" + decodedUrl + "\" target=\"_blank\">" + decodedUrl + "</a>");
                return EVAL_PAGE;
            }
            StringBuffer httpContent = new StringBuffer();
            httpContent.append("<!-- URL : ").append(urlStr).append("-->");
            String tmpLine;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((tmpLine = reader.readLine()) != null) {
                    httpContent.append(tmpLine);
                }
                reader.close();
            } catch (IOException ioe) {
                user.resetSourceURL();
                String decodedUrl = URLDecoder.decode(urlStr, "UTF-8");
                pageContext.getOut().write("Unable to display the source at : <a href=\"" + decodedUrl + "\" target=\"_blank\">" + decodedUrl + "</a>");
                return EVAL_PAGE;
            }
            pageContext.getOut().write(httpContent.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspException("Error when trying to get HTTP content");
        }
        return EVAL_PAGE;
    }
