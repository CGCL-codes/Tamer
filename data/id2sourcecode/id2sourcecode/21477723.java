    public RequestWrapper(HttpServletRequest req, IURLMapper mapper) throws IOException {
        super(req);
        this.req = req;
        parameterMap = new HashMap();
        this.mapper = mapper;
        initializeParameterMap();
        InputStream is = req.getInputStream();
        baos = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int letti;
        while ((letti = is.read(buf)) > 0) baos.write(buf, 0, letti);
        buffer = baos.toByteArray();
    }
