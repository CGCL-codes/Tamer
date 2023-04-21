    private String performPayPalApiCall(final String callParams) throws IOException {
        LOG.info("PayPalExpressCheckoutPaymentGatewayImpl#performPayPalApiCall call parameters :" + callParams);
        final StringBuilder respBuilder = new StringBuilder();
        final HttpPost httpPost = new HttpPost(getParameterValue(PP_EC_API_URL));
        httpPost.setEntity(new StringEntity(callParams));
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpResponse response = client.execute(httpPost);
        final BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String _line;
        while (((_line = rd.readLine()) != null)) {
            respBuilder.append(_line);
        }
        LOG.info("PayPalExpressCheckoutPaymentGatewayImpl#performPayPalApiCall responce :" + respBuilder.toString());
        return respBuilder.toString();
    }
