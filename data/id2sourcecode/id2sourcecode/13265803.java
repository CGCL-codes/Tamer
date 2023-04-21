    public Specification getInterfaceSpecification(UUID location) {
        if (context != null) try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(location.getValue());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String wsdlString = EntityUtils.toString(entity);
                Map<ISyntaxConverter.SyntaxConverterType, ISyntaxConverter> scs = context.getSyntaxConverters();
                ISyntaxConverter syntaxConverter = scs.get(ISyntaxConverter.SyntaxConverterType.SLASOISyntaxConverter);
                return syntaxConverter.parseWSDL(wsdlString)[0];
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
