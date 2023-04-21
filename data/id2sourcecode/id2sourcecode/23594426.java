    public static void answerBack(String alertId, String signal, String feedbackUrl) {
        if (signal != null) {
            logger.info("Sending signal " + signal + " for alertId " + alertId + " to the alerting application  on URL " + feedbackUrl);
            String finalUrl = feedbackUrl + "?alertId=" + alertId + "&action=" + signal;
            try {
                URL url = new URL(finalUrl);
                InputStream in = url.openConnection().getInputStream();
                byte[] buffer = new byte[in.available()];
                int len = in.read(buffer);
                String httpResponse = "";
                for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
                logger.info("Received the follwing HTTP response: " + httpResponse);
            } catch (Exception e) {
                logger.error("couldn't connect to " + finalUrl + " : " + e.getMessage());
            }
        } else {
            logger.debug("signal is null, not sending anything to feedback URL");
        }
    }
