    public void getDescription(String id, boolean recursive, ArrayList<String> processed) throws Exception {
        String path = translate(id);
        ArrayList<String> idList = null;
        processed.add(id);
        if (path != null) {
            path += ".xml";
            File file = new File(path);
            if (file.exists()) {
                idList = streamContent(path, recursive);
            } else {
                mOut.println("<Message>Unable to locate resource within known authority. Looking for: " + id + "</Message>");
            }
        } else {
            if (mHigherAuthority != null) {
                String buffer = "";
                String url = mHigherAuthority + getURLParameters(id);
                URL urlSource = new URL(url);
                URLConnection con = urlSource.openConnection();
                InputStream stream = con.getInputStream();
                idList = streamContent(stream, recursive);
            } else {
                mOut.println("<Message>Unable to locate authority for: " + id + "</Message>");
            }
        }
        if (idList != null) {
            for (String item : idList) {
                mOut.println("");
                if (!igpp.util.Text.isInList(item, processed)) {
                    getDescription(item, recursive, processed);
                }
            }
        }
    }
