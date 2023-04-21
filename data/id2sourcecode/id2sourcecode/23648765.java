    private String writeResult(SnmpAssertionResult result, String thread, String label, Date startTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-HHmmssSSS");
            String resultFileName = sdf.format(startTime) + "-" + label + ".html";
            resultFileName = resultFileName.replace(' ', '_').replace(':', '-');
            String htmlBasePath = getHtmlBasePath() + java.io.File.separator;
            String htmlResourcePath = htmlBasePath + thread + java.io.File.separator;
            (new File(htmlResourcePath)).mkdir();
            String fqResultFileName = htmlResourcePath + resultFileName;
            FileOutputStream resFos = new FileOutputStream(fqResultFileName);
            resFos.write(result.getResponseData());
            resFos.flush();
            resFos.close();
            Debug.debug("Wrote " + fqResultFileName);
            return resultFileName;
        } catch (Exception e) {
            log.error("Could open result file", e);
        }
        return null;
    }
