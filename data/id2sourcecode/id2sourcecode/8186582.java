    public Map<Date, List<Transmission>> getTransmissions(Date day) {
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
        if (daysAfterToday < 0 || daysAfterToday > 7) {
            return null;
        }
        Map<Date, List<Transmission>> retValue = new HashMap<Date, List<Transmission>>();
        long htmlNumber = daysAfterToday / 2 + 1;
        Date firstDate;
        Date secondDate;
        if (daysAfterToday % 2 == 0) {
            firstDate = cal.getTime();
            cal.add(Calendar.DATE, 1);
            secondDate = cal.getTime();
        } else {
            secondDate = cal.getTime();
            cal.add(Calendar.DATE, -1);
            firstDate = cal.getTime();
        }
        Reader reader = null;
        try {
            URL url = new URL(BASE_URL + htmlNumber + ".shtml");
            InputStream is = url.openStream();
            reader = new InputStreamReader(is, "UTF-8");
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(reader));
            Document document = parser.getDocument();
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression dayExpression = xpath.compile("//DIV[@class='day']");
            NodeList dayNodeList = (NodeList) dayExpression.evaluate(document, XPathConstants.NODESET);
            DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
            numberFormat.applyPattern("00");
            retValue.put(firstDate, getTransmissions(firstDate, (Element) dayNodeList.item(0), numberFormat));
            retValue.put(secondDate, getTransmissions(secondDate, (Element) dayNodeList.item(1), numberFormat));
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
        return retValue;
    }
