    private String getDirectionsFromGoogle(EnderecoVO origin, EnderecoVO destination, List<EnderecoVO> waypoints) throws RotaException, ConnectException {
        String inputLine;
        StringBuffer urlsb = new StringBuffer("http://maps.google.com/maps/api/directions/json?");
        urlsb.append("origin=" + retornaLatitudeLongitude(origin));
        urlsb.append("&destination=" + retornaLatitudeLongitude(destination));
        if (waypoints.size() > 0) {
            urlsb.append("&waypoints=optimize:true");
            waypoints.iterator();
            for (EnderecoVO e : waypoints) {
                urlsb.append("|" + retornaLatitudeLongitude(e));
            }
        }
        urlsb.append("&sensor=false");
        logger.debug("URL directions, c�lculo da rota: " + urlsb.toString());
        HttpURLConnection httpConnection = null;
        String proxy = resourceBundle.getString(Constants.PROXY_HOST);
        String port = resourceBundle.getString(Constants.PROXY_PORT);
        try {
            String addressUTF8 = urlsb.toString();
            URL url = new URL(addressUTF8);
            Properties systemproperties = System.getProperties();
            if (proxy != null && !proxy.equals("")) {
                systemproperties.setProperty("http.proxyHost", proxy);
                systemproperties.setProperty("http.proxyPort", port);
            }
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();
            inputLine = StringUtils.convertStreamToString(httpConnection.getInputStream());
            httpConnection.disconnect();
        } catch (UnsupportedEncodingException ue) {
            logger.error(ue);
            throw new RotaException("Encoding n�o suportado : " + ue.getMessage());
        } catch (MalformedURLException ma) {
            logger.error(ma);
            throw new RotaException("Erro na URL : " + ma.getMessage());
        } catch (IOException io) {
            logger.error(io);
            throw new RotaException("Erro de io : ", io);
        } catch (Exception ex) {
            throw new RotaException("N�o foi poss�vel gerar a rota  : " + ex.getMessage());
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return inputLine;
    }
