    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        out = response.getWriter();
        String wsmlOntology = "";
        String wsmlQuery = "";
        System.gc();
        String eval = request.getParameter("eval");
        if (eval != null) {
            evalmethod = Integer.parseInt(eval);
        }
        String rsnr = request.getParameter("reasoner");
        if (rsnr != null) {
            if (rsnr.equals("kaon")) {
                reasoner = WSMLReasonerFactory.BuiltInReasoner.KAON2;
            }
        }
        boolean inFrame = request.getParameter("inframe") != null;
        try {
            if (request.getParameter("url") != null && request.getParameter("url").indexOf("[url]") == -1) {
                URL url = new URL(request.getParameter("url"));
                InputStream in = url.openStream();
                int k;
                byte buff[] = new byte[bufferSize];
                OutputStream xOutputStream = new ByteArrayOutputStream(bufferSize);
                while ((k = in.read(buff)) != -1) {
                    xOutputStream.write(buff, 0, k);
                }
                wsmlOntology = xOutputStream.toString();
            } else if (request.getParameter("wsmlOntology") != null) {
                wsmlOntology = request.getParameter("wsmlOntology");
            }
            if (request.getParameter("wsmlQuery") != null) {
                wsmlQuery = request.getParameter("wsmlQuery");
            }
            out.println("<!DOCTYPE html PUBLIC '-W3CDTD HTML 4.01 TransitionalEN'>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>DERI WSML Reasoning result</title>");
            out.println("  <link rel='shortcut icon' href='favicon.ico'/>");
            out.println("  <link rel='stylesheet' type='text/css' href='wsml.css'/>");
            out.println("</head>");
            out.println("<body><div class=\"box\">");
            if (wsmlOntology.length() == 0) {
                error("No Ontology found, enter ontology ");
            } else if (wsmlQuery.length() == 0) {
                error("No Query found, enter Query ");
            } else {
                try {
                    doReasoning(wsmlQuery, wsmlOntology, inFrame);
                } catch (Exception e) {
                    error("Error:", e);
                }
            }
        } catch (MalformedURLException e) {
            error("Input URL malformed: " + request.getParameter("url"));
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }
        out.println("</div></body>");
        out.println("</html>");
    }
