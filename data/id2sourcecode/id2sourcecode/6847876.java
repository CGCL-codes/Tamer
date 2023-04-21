    public static void printBodyFrom(URL url, PrintWriter out) {
        if (url == null) return;
        try {
            URLConnection con = url.openConnection();
            con.setDoInput(true);
            InputStream is = con.getInputStream();
            String ContentType = con.getContentType();
            String ContentEncoding = con.getContentEncoding();
            boolean grabbody = true;
            if (ContentType != null) {
                if (!ContentType.equals("text/html")) {
                    grabbody = false;
                }
            } else grabbody = false;
            InputStreamReader isr = null;
            if (ContentEncoding != null) isr = new InputStreamReader(is, ContentEncoding); else isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            boolean MustWrite = !grabbody;
            String line;
            boolean LookForClosure = false;
            while ((line = br.readLine()) != null) {
                if (MustWrite) {
                    if (grabbody) {
                        String lowercaseline = line.toLowerCase();
                        if (LookForClosure) {
                            int beginindex = line.indexOf(">");
                            if (beginindex == -1) continue;
                            LookForClosure = false;
                            if (beginindex + 1 < line.length()) {
                                int endindex = lowercaseline.indexOf("</body>");
                                if (endindex == -1) out.println(line.substring(beginindex + 1, line.length())); else if (beginindex + 1 < endindex) out.println(line.substring(beginindex + 1, endindex));
                            }
                            continue;
                        }
                        int index = lowercaseline.indexOf("</body>");
                        if (index == -1) {
                            out.println(line);
                            continue;
                        }
                        out.println(line.substring(0, index));
                        break;
                    }
                    out.println(line);
                    continue;
                }
                String lowercaseline = line.toLowerCase();
                int index = lowercaseline.indexOf("<body");
                if (index == -1) continue;
                MustWrite = true;
                int beginindex = lowercaseline.indexOf(">", index);
                if (beginindex == -1) {
                    LookForClosure = true;
                    continue;
                }
                if (beginindex + 1 < line.length()) {
                    int endindex = lowercaseline.indexOf("</body>");
                    if (endindex == -1) out.println(line.substring(beginindex + 1, line.length())); else if (beginindex + 1 < endindex) out.println(line.substring(beginindex + 1, endindex));
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
