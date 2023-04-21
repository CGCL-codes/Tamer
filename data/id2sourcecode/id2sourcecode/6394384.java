    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/xml");
        InputStreamReader isr = new InputStreamReader(request.getInputStream());
        OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
        char[] buf = new char[4096];
        int read = 0;
        while ((read = isr.read(buf, 0, buf.length)) != -1) {
            osw.write(buf, 0, read);
        }
        isr.close();
        osw.flush();
        osw.close();
    }
