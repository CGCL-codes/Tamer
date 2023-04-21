    public Document sendDigVerb(Document digVerb, DIGProfile profile) {
        try {
            Element verb = digVerb.getDocumentElement();
            if (!verb.hasAttribute(DIGProfile.URI)) {
                verb.setAttribute(DIGProfile.URI, m_kbURI);
            }
            URL url = new URL(m_extReasonerURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            StringWriter out = new StringWriter();
            serialiseDocument(digVerb, out);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(out.getBuffer().length()));
            conn.setRequestProperty("Content-Type", profile.getContentType());
            logMessage(true, digVerb);
            conn.connect();
            PrintWriter pw = FileUtils.asPrintWriterUTF8(conn.getOutputStream());
            pw.print(out.getBuffer());
            pw.flush();
            pw.close();
            Document response = getDigResponse(conn);
            logMessage(false, response);
            errorCheck(response, profile);
            return response;
        } catch (IOException e) {
            throw new DIGWrappedException(e);
        }
    }
