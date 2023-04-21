    private String findLatestVersion(String ecVersion) {
        String latestVersion = ecVersion;
        try {
            String html = null;
            {
                URL url = new URL(downloadsPageUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int read;
                while ((read = in.read()) != -1) {
                    out.write(read);
                }
                in.close();
                html = new String(out.toByteArray());
            }
            int latest = Integer.parseInt(ecVersion);
            Matcher m = jarUrlRegex.matcher(html);
            while (m.find()) {
                int cur = Integer.parseInt(m.group(1));
                if (cur > latest) {
                    latest = cur;
                    latestVersion = m.group(1);
                }
            }
        } catch (MalformedURLException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(sw.toString());
        } catch (UnknownHostException e) {
            callback.updateCheckFailed();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.severe(sw.toString());
        }
        return latestVersion;
    }
