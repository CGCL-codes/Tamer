    public static String checkSignature(URL targeturl, String signature, String[] engines) {
        char[] buf = new char[20 * 1024];
        String qwords = URIs.encode(signature);
        URL url = null;
        for (int i = 0, imax = engines.length; i < imax; i++) {
            String engine = engines[i];
            char ranking = engine.charAt(0), allwords = engine.charAt(1);
            if (allwords != ' ') {
                StringBuffer awsb = new StringBuffer(100);
                StringTokenizer awst = new StringTokenizer(signature);
                while (awst.hasMoreTokens()) awsb.append(allwords).append(awst.nextToken()).append(' ');
                qwords = URIs.encode(awsb.toString().trim());
            }
            engine = engine.substring(2);
            try {
                url = new URL(engine + qwords);
                Reader in = new BufferedReader(new InputStreamReader(url.openStream()));
                int len = 0, hunk;
                while (len < buf.length && (hunk = in.read(buf, len, buf.length - len)) > 0) len += hunk;
                in.close();
                String result = new String(buf, 0, len);
                String hit = "not indexed (yet?)";
                String searchfor = URIs.encode(targeturl.getHost() + targeturl.getFile());
                String filename = targeturl.getFile();
                if (filename.indexOf('/') != -1) filename = filename.substring(filename.lastIndexOf(',') + 1);
                int inx = result.indexOf(searchfor);
                if (inx == -1) {
                    searchfor = targeturl.getHost();
                    inx = result.indexOf(searchfor);
                }
                if (inx == -1 && filename.length() >= 5) {
                    searchfor = filename;
                    inx = result.indexOf(searchfor);
                }
                if (inx == -1) {
                }
                int rank = -1;
                if (inx != -1) {
                    int endtag = result.indexOf('>', inx);
                    hit = result.substring(inx, endtag);
                    if (ranking == '1') {
                        int digend = -1;
                        for (int j = inx; j >= 2; j--) {
                            char ch = result.charAt(j);
                            if (digend == -1 && (ch == ' ' || ch == '&') && result.charAt(j - 1) == '.' && Character.isDigit(result.charAt(j - 2))) {
                                j -= 2;
                                digend = j;
                            } else if (digend != -1 && !Character.isDigit(ch)) {
                                try {
                                    rank = Integer.parseInt(result.substring(j + 1, digend + 1));
                                } catch (NumberFormatException nfe) {
                                }
                                break;
                            }
                        }
                    }
                    if (rank == -1) {
                        rank = 1;
                        String curhost = url.getHost();
                        int inx2 = curhost.indexOf('.');
                        if (inx2 != -1) curhost = curhost.substring(inx2 + 1);
                        for (int j = (ranking == 'o' ? result.indexOf("<OL") : 0), jmax = inx - 20; j < jmax; j++) {
                            int newj = result.indexOf("<a href", j);
                            if (newj == -1) newj = result.indexOf("<A HREF=", j);
                            if (newj == -1 || newj >= jmax) break;
                            j = newj;
                            newj = result.indexOf(curhost, j);
                            if (newj != -1 && newj > endtag) rank++;
                        }
                    }
                }
                String report = url.getHost();
                if (allwords != ' ') report += "  (strict)";
                if (rank != -1 && rank <= 10) report += "  rank=" + rank;
                report += "\t" + hit;
                System.out.println(report);
            } catch (MalformedURLException male) {
            } catch (IOException ioe) {
                System.err.println(url.toString() + ": " + ioe.toString());
            }
        }
        return null;
    }
