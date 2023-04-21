    @Override
    public String execute(HttpRequest request, HttpResponse response) throws Exception {
        URL addr = new URL("http://localhost:8080/MerakCore/Main?b=RequestFile");
        URLConnection conn = addr.openConnection();
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("content-disposition", "attachment; filename=hello.txt");
        conn.setRequestProperty("b", "SendFile");
        conn.setDoOutput(true);
        PrintWriter pw = new PrintWriter(conn.getOutputStream());
        pw.println("Olll� Enfermeira!!");
        pw.close();
        conn.getInputStream();
        return view;
    }
