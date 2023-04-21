    public void work(HTTPRequest request, HTTPResponse response) throws Exception {
        try {
            File file = new File(request.env.get("SCRIPT_FILENAME"));
            FileReader reader = new FileReader(file);
            HashMap<String, Object> binding = new HashMap();
            binding.put("request", request);
            binding.put("response", response);
            PrintWriter writer = response.getOutputWriter();
            eng.createTemplate(reader).make(binding).writeTo(writer);
            response.setStatus(200);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            response.setStatus(404);
        }
    }
