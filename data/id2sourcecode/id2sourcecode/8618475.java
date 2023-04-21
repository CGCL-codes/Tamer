    public static void ToHTML(String prefix, GeoSciML_Mapping mapping, String strTemplate, String strRequest, String tagFeature, String lang, PrintWriter sortie) throws Exception {
        String level = "info.";
        if (ConnectorServlet.debug) level = "debug.";
        Log log = LogFactory.getLog(level + "fr.brgm.exows.gml2gsml.Gml2out");
        strRequest = StringUtils.replace(strRequest, "text/html", "application/vnd.ogc.gml");
        URL url2Request = new URL(strRequest);
        URLConnection conn = url2Request.openConnection();
        Date dDebut = new Date();
        BufferedReader buffin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer strBuffer = new StringBuffer();
        String strLine = null;
        Template template = VelocityCreator.createTemplate("/fr/brgm/exows/gml2gsml/templates/" + strTemplate);
        boolean newFeature = false;
        strBuffer = new StringBuffer("");
        int nbFeatures = 0;
        strLine = buffin.readLine();
        String strTmp;
        int index;
        int length = tagFeature.length() + 1;
        while (strLine != null) {
            index = strLine.indexOf(tagFeature);
            if (index != -1) {
                if (newFeature) {
                    nbFeatures++;
                    VelocityContext context = new VelocityContext();
                    strBuffer.append(strLine.substring(0, index - 2));
                    strBuffer.append("</" + tagFeature + ">\n");
                    GSMLFeatureGeneric feature = createGSMLFeatureFromGMLFeatureString(prefix, mapping, strBuffer.toString(), null);
                    context.put("feature", feature);
                    context.put("lang", lang);
                    context.put("vocabularies", new Vocabularies());
                    String outputFeatureMember = VelocityCreator.createXMLbyContext(context, template);
                    sortie.println(outputFeatureMember);
                    strBuffer = new StringBuffer();
                    newFeature = false;
                } else {
                    newFeature = true;
                }
            }
            strTmp = strLine;
            strLine = buffin.readLine();
            if (strLine == null) {
                try {
                    if (strTmp.length() > index + length) strLine = strTmp.substring(index + length);
                } catch (Exception e) {
                }
                if (newFeature) {
                    strBuffer.append("<" + tagFeature + ">\n");
                }
            } else {
                if (newFeature) {
                    strBuffer.append(strLine);
                    strBuffer.append("\n");
                }
            }
        }
        buffin.close();
        Date dFin = new Date();
        String output = "GEOSCIML : " + nbFeatures + " features handled - time : " + (dFin.getTime() - dDebut.getTime()) / 1000 + " [" + dDebut + " // " + dFin + "]";
        log.trace(output);
    }
