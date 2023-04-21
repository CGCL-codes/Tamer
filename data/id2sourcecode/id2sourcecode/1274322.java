    public static void sendPDFResponse(HttpServletRequest request, HttpServletResponse response, DocumentHandle doc) throws Exception {
        OutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        response.setHeader("expires", "Sun, 30 March 1969 7:00:00 GMT");
        String width = request.getParameter("print_width");
        String height = request.getParameter("print_height");
        int w = -1, h = -1;
        try {
            if (width != null) w = Integer.parseInt(width);
            if (height != null) h = Integer.parseInt(height);
        } catch (Exception e) {
            ;
        }
        File fx = null;
        if ((w > -1) && (h > -1)) if (doc instanceof WebWorkBook) fx = ExtenXLS.getPDF((WebWorkBook) doc, h, w); else if (doc instanceof WebWorkBook) fx = ExtenXLS.getPDF((WebWorkBook) doc);
        FileInputStream fin = new FileInputStream(fx);
        BufferedOutputStream bout = new BufferedOutputStream(out);
        for (int t = 0; t < fx.length(); t++) {
            bout.write(fin.read());
        }
        bout.flush();
        bout.close();
    }
