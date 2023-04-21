    @RequestMapping(value = "/**/dataTCGAExternalTemplate", method = RequestMethod.GET)
    @ModelAttribute
    public void processDataTCGAExternalTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("Begin processDataTCGAExternal(" + request.getRequestURI() + ")");
        List<String> poorPatients = new ArrayList<String>();
        poorPatients.addAll(this.getBoylePatientSet(request, "Poor"));
        String[] bamFiles = poorPatients.toArray(new String[poorPatients.size()]);
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        InputStream is = servletContext.getResourceAsStream("/templates/jboyle_external.template");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                if (nextLine.trim().equals("@PATIENT_BAMS")) {
                    if (bamFiles.length > 0) {
                        for (String bamFile : bamFiles) {
                            log.info("bam files for patientId:" + bamFile);
                            String[] splitted = bamFile.split("/");
                            out.println("<Resource name=\"" + splitted[splitted.length - 1] + "\" path=\"" + bamFile + "\"/>");
                        }
                        out.println("</Category>");
                    }
                } else {
                    out.println(nextLine);
                }
            }
        } finally {
            out.close();
            br.close();
            is.close();
        }
        log.info("Done processDataTCGAExternal(" + request.getRequestURI() + ")");
    }
