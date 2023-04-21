    private int checkHTTP(URL url) {
        try {
            URLConnection connect = url.openConnection();
            String response = connect.getHeaderField(0);
            if (response != null) {
                StringTokenizer tokens = new StringTokenizer(response);
                if (tokens.countTokens() >= 2) {
                    tokens.nextToken();
                    String codeStr = tokens.nextToken();
                    try {
                        int code = Integer.parseInt(codeStr);
                        if ((code / 100) == 2) {
                            return SpiderStatus.SUCCESS;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
        }
        return SpiderStatus.INACCESSIBLE;
    }
