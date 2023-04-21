    public void actionListener(ActionEvent e) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("text/HTML");
            HtmlOutputFormat formatter = super.getSelectedFormatAsInstance();
            Reader reader = new StringReader(this.sourceText);
            Writer writer = new OutputStreamWriter(out);
            formatter.format(reader, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        fc.renderResponse();
        fc.responseComplete();
    }
