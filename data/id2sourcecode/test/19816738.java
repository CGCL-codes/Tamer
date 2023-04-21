    @Override
    public String execute(HttpRequest request, HttpResponse response) throws Exception {
        WebParameter p = request.getOctetStream();
        System.out.println(p.getName());
        System.out.println(p.getValue());
        File output = new File("c://octet.txt");
        p.getFile(output);
        return view;
    }
