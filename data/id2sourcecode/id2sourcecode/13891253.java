    public List<Transmission> getTransmissions(Channel channel, Date day) {
        MessageFormat format = new MessageFormat(pattern);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        cal.setTime(day);
        DateFormat yearFormat = new SimpleDateFormat("yy");
        String urlString = format.format(new Object[] { channel.getCode(), yearFormat.format(day), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE) });
        Reader reader = null;
        List<Transmission> transmissions = new ArrayList<Transmission>();
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
            JSONArray jsonArray = jsonObject.getJSONArray("plan");
            DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getInstance();
            numberFormat.applyPattern("00");
            boolean sameDay = true;
            int lastTime = 0;
            for (int i = 0; i < jsonArray.length() && sameDay; i++) {
                JSONObject transmissionJson = jsonArray.getJSONObject(i);
                int id = transmissionJson.getInt("id");
                if (id > 0 && sameDay) {
                    String timeString = transmissionJson.getString("starttime");
                    String hour = timeString.substring(0, 2);
                    String minutes = timeString.substring(3, 5);
                    int hours = numberFormat.parse(hour).intValue();
                    int minute = numberFormat.parse(minutes).intValue();
                    int currentTime = hours * 60 + minute;
                    if (currentTime >= lastTime) {
                        lastTime = currentTime;
                        cal.setTime(day);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        cal.set(Calendar.HOUR_OF_DAY, hours);
                        cal.set(Calendar.MINUTE, minute);
                        transmissions.add(new Transmission(transmissionJson.getString("title"), transmissionJson.getString("desc"), cal.getTime(), null, "http://guidatv.sky.it/guidatv/programma/" + transmissionJson.getString("genre").replaceAll(" ", "") + "/" + transmissionJson.getString("subgenre").replaceAll(" ", "") + "/" + transmissionJson.getString("normalizedtitle") + "_" + transmissionJson.getString("pid") + ".shtml?eventId=" + id));
                    } else {
                        sameDay = false;
                    }
                }
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
        return transmissions;
    }
