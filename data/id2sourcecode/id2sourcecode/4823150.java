    public void printExecute(HttpUriRequest request) {
        HttpResponse response = execute(request);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        try {
            if (resEntity != null) {
                System.out.println(EntityUtils.toString(resEntity));
            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }
        } catch (IOException e) {
            System.err.println("IOExe1: " + e.getMessage());
            e.printStackTrace();
        }
        closeConnection();
    }
