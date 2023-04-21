    private String resolveParameters(String code) throws ParseException, Exception {
        String[] strParms;
        while ((strParms = XMLHelper.getParametersFromText(code)).length > 0) {
            for (String element : strParms) {
                String parmValue = null;
                if (element.startsWith("randomDate(")) {
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    sdf.applyPattern("yyyyMMdd");
                    String res = randomParams.get(element);
                    if (res != null) parmValue = res; else {
                        String parts[] = element.split("\\(");
                        String[] opts = parts[1].substring(0, parts[1].length() - 1).split(",");
                        Long minValue = sdf.parse(opts[0].trim()).getTime();
                        Long maxValue = sdf.parse(opts[1].trim()).getTime();
                        double range = maxValue - minValue;
                        long offSet = (long) (this.rnd.nextDouble() * range);
                        parmValue = sdf.format(new Date(minValue + offSet));
                        randomParams.put(element, parmValue);
                    }
                } else if (element.startsWith("random(")) {
                    String res = randomParams.get(element);
                    if (res != null) parmValue = res; else {
                        String parts[] = element.split("\\(");
                        String[] opts = parts[1].substring(0, parts[1].length() - 1).split(",");
                        int minValue = Integer.parseInt(opts[0].trim());
                        int maxValue = Integer.parseInt(opts[1].trim());
                        int maxRnd = maxValue - minValue;
                        parmValue = Integer.toString(this.rnd.nextInt(maxRnd) + minValue);
                        randomParams.put(element, parmValue);
                    }
                } else if (element.equalsIgnoreCase("seed")) parmValue = Integer.toString(this.rnd.nextInt(100)); else if (element.equalsIgnoreCase("execution_id")) {
                    parmValue = Integer.toString(this.parentExecutionId);
                } else parmValue = this.getParameter(element, true);
                if (parmValue != null) {
                    code = XMLHelper.replaceParameter(code, element, parmValue);
                } else {
                    throw new Exception("Parameter " + element + " can not be found in parameters");
                }
            }
        }
        return code;
    }
