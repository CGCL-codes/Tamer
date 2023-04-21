    private boolean preparePost() {
        URL url = null;
        URLConnection urlConn = null;
        int code = 0;
        boolean gotNumReplies = false;
        boolean gotSeqNum = false;
        boolean gotSesc = false;
        try {
            url = new URL("http://" + m_host + "/forums/index.php?action=post;topic=" + m_gameId + ".0;num_replies=" + m_numReplies);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            urlConn = url.openConnection();
            ((HttpURLConnection) urlConn).setRequestMethod("GET");
            ((HttpURLConnection) urlConn).setInstanceFollowRedirects(false);
            urlConn.setDoOutput(false);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Host", m_host);
            urlConn.setRequestProperty("Accept", "*/*");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Cache-Control", "no-cache");
            if (m_referer != null) urlConn.setRequestProperty("Referer", m_referer);
            if (m_cookies != null) urlConn.setRequestProperty("Cookie", m_cookies);
            m_referer = url.toString();
            readCookies(urlConn);
            code = ((HttpURLConnection) urlConn).getResponseCode();
            if (code != 200) {
                String msg = ((HttpURLConnection) urlConn).getResponseMessage();
                m_turnSummaryRef = String.valueOf(code) + ": " + msg;
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line = "";
            Pattern p_numReplies = Pattern.compile(".*?<input type=\"hidden\" name=\"num_replies\" value=\"(\\d+)\" />.*");
            Pattern p_seqNum = Pattern.compile(".*?<input type=\"hidden\" name=\"seqnum\" value=\"(\\d+)\" />.*");
            Pattern p_sesc = Pattern.compile(".*?<input type=\"hidden\" name=\"sc\" value=\"(\\w+)\" />.*");
            while ((line = in.readLine()) != null) {
                if (!gotNumReplies) {
                    Matcher match_numReplies = p_numReplies.matcher(line);
                    if (match_numReplies.matches()) {
                        m_numReplies = match_numReplies.group(1);
                        gotNumReplies = true;
                        continue;
                    }
                }
                if (!gotSeqNum) {
                    Matcher match_seqNum = p_seqNum.matcher(line);
                    if (match_seqNum.matches()) {
                        m_seqNum = match_seqNum.group(1);
                        gotSeqNum = true;
                        continue;
                    }
                }
                if (!gotSesc) {
                    Matcher match_sesc = p_sesc.matcher(line);
                    if (match_sesc.matches()) {
                        m_sesc = match_sesc.group(1);
                        gotSesc = true;
                        continue;
                    }
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (!gotNumReplies || !gotSeqNum || !gotSesc) {
            m_turnSummaryRef = "Error: ";
            if (!gotNumReplies) m_turnSummaryRef += "No num_replies found in A&A.org post form. ";
            if (!gotSeqNum) m_turnSummaryRef += "No seqnum found in A&A.org post form. ";
            if (!gotSesc) m_turnSummaryRef += "No sc found in A&A.org post form. ";
            return false;
        }
        return true;
    }
