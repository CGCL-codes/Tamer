    @Override
    public List<Transmission> getTransmissions(Channel channel, Date day) {
        String baseUrl = channel2url.get(channel.getCode());
        String path = channel2path.get(channel.getCode());
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Rome");
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(day);
        cal.setTimeZone(timeZone);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar nowCal = Calendar.getInstance(timeZone);
        nowCal.set(Calendar.HOUR_OF_DAY, 0);
        nowCal.set(Calendar.MINUTE, 0);
        nowCal.set(Calendar.SECOND, 0);
        nowCal.set(Calendar.MILLISECOND, 0);
        long daysAfterToday = (cal.getTimeInMillis() - nowCal.getTimeInMillis()) / ONE_DAY;
        if (daysAfterToday < 0 || daysAfterToday > 6) {
            return null;
        }
        String htmlNumber;
        if (daysAfterToday == 0) {
            htmlNumber = "";
        } else {
            htmlNumber = "_" + Long.toString(daysAfterToday);
        }
        Reader reader = null;
        try {
            URL url = new URL(baseUrl + htmlNumber + ".html");
            InputStream is = url.openStream();
            reader = new InputStreamReader(is, "UTF-8");
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(reader));
            Document document = parser.getDocument();
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NamespaceContextImpl());
            DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
            numberFormat.applyPattern("00");
            XPathExpression dayExpression = xpath.compile(path);
            NodeList dayNodeList = (NodeList) dayExpression.evaluate(document, XPathConstants.NODESET);
            return getTransmissions(day, dayNodeList, numberFormat, xpath);
        } catch (MalformedURLException e) {
            throw new GuidaTvException(e);
        } catch (IOException e) {
            throw new GuidaTvException(e);
        } catch (SAXException e) {
            throw new GuidaTvException(e);
        } catch (XPathExpressionException e) {
            throw new GuidaTvException(e);
        } catch (ParseException e) {
            throw new GuidaTvException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
