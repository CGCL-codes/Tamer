    public void service(Connection conn) throws IOException {
        UserInstance userInstance = (UserInstance) conn.getUserInstance();
        if (userInstance == null) {
            serviceBadRequest(conn, "No container available.");
            return;
        }
        String listModelId = conn.getRequest().getParameter(PARAMETER_LIST_MODEL_UID);
        if (listModelId == null) {
            serviceBadRequest(conn, "List Model UID not specified.");
            return;
        }
        String query = conn.getRequest().getParameter(PARAMETER_STARTS_WITH);
        String limit = conn.getRequest().getParameter(PARAMETER_LIMIT);
        String start = conn.getRequest().getParameter(PARAMETER_START);
        if (query == null || limit == null || start == null) {
            InputStream in = conn.getRequest().getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read = -1;
            do {
                read = in.read(buf);
                if (read > 0) {
                    baos.write(buf, 0, read);
                }
            } while (read > 0);
            String text = baos.toString();
            if (text != null) {
                Hashtable<String, String[]> table = HttpUtils.parseQueryString(text);
                if (query == null) {
                    String[] vals = table.get(PARAMETER_STARTS_WITH);
                    query = vals[0];
                }
                if (limit == null) {
                    String[] vals = table.get(PARAMETER_LIMIT);
                    limit = vals[0];
                }
                if (start == null) {
                    String[] vals = table.get(PARAMETER_START);
                    start = vals[0];
                }
            }
        }
        Integer limitVal = limit == null ? -1 : Integer.valueOf(limit);
        Integer startVal = start == null ? 0 : Integer.valueOf(start);
        RemoteAutocompleteModel imageReference = getModel(userInstance, listModelId);
        if (imageReference == null) {
            serviceBadRequest(conn, "List Model UID is not valid.");
            return;
        }
        renderModel(conn, imageReference, query, limitVal, startVal);
    }
