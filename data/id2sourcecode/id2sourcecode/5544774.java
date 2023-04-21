    private void header(String language, String style, String embedCss, Writer out) throws IOException {
        out.write("<html><head>");
        String cssResourceName = "css/languages/" + language + "/" + style;
        if ("on".equals(embedCss)) {
            out.write("<style type=\"text/css\" media=\"screen\">");
            InputStream is = getServletContext().getResourceAsStream("/" + cssResourceName);
            int ch;
            while ((ch = is.read()) != -1) out.write(ch);
            out.write("</style>");
        } else {
            out.write("<link rel=\"stylesheet\" href=\"" + cssResourceName + "\" type=\"text/css\" />");
        }
        out.write("</head><body>");
        cutHere(out);
    }
