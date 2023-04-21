    private void loadTransmissions(String type, Date day, Date urlDay, MessageFormat format, List<Transmission> transmissions) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        cal.setTime(urlDay);
        String urlString = format.format(new Object[] { type, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE) });
        Reader reader = null;
        try {
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            reader = new InputStreamReader(is, "UTF-8");
            StringBuilder builder = new StringBuilder();
            int character;
            while ((character = reader.read()) > 0) {
                builder.append((char) character);
            }
            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("contents");
            DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
            numberFormat.applyPattern("00");
            boolean sameDay = true;
            for (int i = 0; i < jsonArray.length() && sameDay; i++) {
                JSONObject transmissionJson = jsonArray.getJSONObject(i);
                String timeString = transmissionJson.getString("time");
                String hour = timeString.substring(0, 2);
                String minutes = timeString.substring(3, 5);
                int hours = numberFormat.parse(hour).intValue();
                int minute = numberFormat.parse(minutes).intValue();
                cal.setTime(day);
                cal.set(Calendar.HOUR_OF_DAY, hours);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                transmissions.add(new Transmission(transmissionJson.getString("name"), transmissionJson.getString("description"), cal.getTime(), null, null));
            }
        } catch (MalformedURLException e) {
            throw new GuidaTvException(e);
        } catch (IOException e) {
            throw new GuidaTvException(e);
        } catch (ParseException e) {
            throw new GuidaTvException(e);
        } catch (JSONException e) {
            log.log(Level.FINE, "Exception when getting schedule", e);
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
