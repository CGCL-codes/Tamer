    public static String metadataToXml(MQContentPackage cp) {
        InputStream is = MQContentPackage.class.getClassLoader().getResourceAsStream("org/qtitools/mathqurate/resources/md-template.xml");
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String template = sb.toString();
        LinkedHashMap<String, String> mdmap = cp.getMetadataMap();
        template = template.replace("{{IDENTIFIER}}", mdmap.get(MQMetadata.IDENTIFIER[0]));
        template = template.replace("{{TITLE}}", mdmap.get(MQMetadata.TITLE[0]));
        template = template.replace("{{DESCRIPTION}}", mdmap.get(MQMetadata.DESCRIPTION[0]));
        template = template.replace("{{AUTHOR}}", mdmap.get(MQMetadata.AUTHOR[0]));
        template = template.replace("{{DESCRIPTION}}", mdmap.get(MQMetadata.DESCRIPTION[0]));
        template = template.replace("{{LOCATION-URL}}", mdmap.get(MQMetadata.LOCATION[0]));
        template = template.replace("{{SOFTWARE}}", mdmap.get(MQMetadata.SOFTWARE[0]));
        template = template.replace("{{CC-URL}}", mdmap.get(MQMetadata.LICENCEURL[0]));
        template = template.replace("{{FETLAR}}", mdmap.get(MQMetadata.REPODOMAIN[0]));
        String keywordsString = mdmap.get(MQMetadata.KEYWORDS[0]);
        if (keywordsString != null) {
            String[] keywords = keywordsString.split(",");
            String xmlString = "";
            for (String keyword : keywords) {
                keyword = keyword.trim();
                xmlString += "<imsmd:keyword><imsmd:langstring xml:lang=\"en\">" + keyword + "</imsmd:langstring></imsmd:keyword>\n";
            }
            template = template.replace("<!-- KEYWORDSHERE -->", xmlString);
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateStr = df.format(date);
        template = template.replace("{{DATE}}", dateStr);
        long epoch = System.currentTimeMillis() / 1000;
        template = template.replace("{{UNIQUE-ID}}", "FETLAR-2-" + String.valueOf(epoch));
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = String.valueOf(epoch).getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            String md5 = String.format("%1$032X", i);
            template = template.replace("{{RANDOM-HASH}}", md5);
        } catch (NoSuchAlgorithmException e) {
        }
        String xmlString = "<imsmd:classification>" + "<imsmd:purpose>" + "<imsmd:source>" + "<imsmd:langstring xml:lang=\"x-none\">imsmdv1.0</imsmd:langstring>" + "</imsmd:source>" + "<imsmd:value>" + "<imsmd:langstring xml:lang=\"x-none\">Discipline</imsmd:langstring>" + "</imsmd:value>" + "</imsmd:purpose>" + "<imsmd:taxonpath>" + "<imsmd:source>" + "<imsmd:langstring xml:lang=\"en\"><!-- TAXONNAMEHERE --></imsmd:langstring>" + "</imsmd:source>" + "<imsmd:taxon>" + "<imsmd:entry>" + "<imsmd:langstring xml:lang=\"en\">" + mdmap.get(MQMetadata.TAXON[0]) + "</imsmd:langstring>" + "</imsmd:entry>" + "</imsmd:taxon>" + "</imsmd:taxonpath>" + "</imsmd:classification>";
        template = template.replace("<!-- MATAXONHERE -->", xmlString);
        template = template.replace("<!-- TAXONNAMEHERE -->", "MathAssess Taxonomy");
        template = template.replace("{{RES-ID}}", "id-" + String.valueOf(UUID.randomUUID()));
        template = template.replace("{{FILENAME}}", mdmap.get(MQMetadata.FILENAME[0]));
        template = template.replaceAll("\\{\\{.+\\}", "");
        int startpoint = template.indexOf("<!-- MDSTART -->");
        int endpoint = template.indexOf("<!-- MDEND -->");
        String metaelement = template.substring(startpoint, endpoint);
        metaelement = metaelement.replace("<!-- MDSTART -->", "");
        String qtiMD = "<imsqti:qtiMetadata>" + "<imsqti:timeDependent>{{TIMEDEPENDENT}}</imsqti:timeDependent>" + "<imsqti:solutionAvailable>{{SOLUTIONAVAILABLE}}</imsqti:solutionAvailable>" + "<imsqti:toolName>Mathqurate</imsqti:toolName>" + "<imsqti:toolVersion>{{MQVERSION}}</imsqti:toolVersion>" + "</imsqti:qtiMetadata>";
        metaelement = metaelement.replace("</metadata>", qtiMD + "</metadata>");
        template = template.replace("<!-- METADATA -->", metaelement);
        template = template.replace("{{TIMEDEPENDENT}}", mdmap.get(MQMetadata.TIMEDEPENDENT[0]));
        template = template.replace("{{SOLUTIONAVAILABLE}}", mdmap.get(MQMetadata.SOLUTIONAVAILABLE[0]));
        template = template.replace("{{MQVERSION}}", mdmap.get(MQMetadata.TOOLVERSION[0]));
        template = template.replaceAll("\\<!--.+--\\>", "");
        return format(template);
    }
