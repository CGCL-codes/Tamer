        public void run() {
            String strUrlDoc;
            String temp_charset = "UTF8";
            String size;
            jTextArea1.append("Crawler started:" + (new Date()).toString() + "\n");
            while ((strUrlDoc = links_queue.getLink()) != null && !stop_thread) {
                temp_charset = "UTF8";
                jTextArea1.append(strUrlDoc + "\n");
                try {
                    buffer = new StringBuffer();
                    URL urlToCrawl = new URL(strUrlDoc);
                    URLConnection urlToCrawlConnection = urlToCrawl.openConnection();
                    urlToCrawlConnection.setRequestProperty("User-Agent", "USER_AGENT");
                    urlToCrawlConnection.setRequestProperty("Referer", "REFERER");
                    urlToCrawlConnection.setUseCaches(false);
                    String content_type = urlToCrawlConnection.getContentType();
                    String charset = temp_charset;
                    if (content_type != null) {
                        StringTokenizer tokens = new StringTokenizer(content_type, " \'\"=");
                        while (tokens.hasMoreTokens()) {
                            if ((tokens.nextToken().toLowerCase().trim()).equals("charset")) {
                                charset = tokens.nextToken().toLowerCase().trim();
                                System.out.println("charset found");
                                break;
                            }
                        }
                    }
                    long date_modified = urlToCrawlConnection.getLastModified();
                    long date_last_crawled = 0;
                    size = String.valueOf(urlToCrawlConnection.getContentLength());
                    if (!isModified(strUrlDoc, date_modified)) {
                        int buffSize = 51200;
                        byte[] buff = new byte[buffSize];
                        charset = charset.toUpperCase();
                        InputStreamReader isr = new InputStreamReader(urlToCrawlConnection.getInputStream(), charset);
                        BufferedReader in = new BufferedReader(isr);
                        int c;
                        while ((c = in.read()) > -1) buffer.append((char) c);
                        if (charset.startsWith("UTF")) links_queue.addLink(parser.parseUTF(buffer.toString(), strUrlDoc, urlToCrawl.getHost(), date_modified, size)); else links_queue.addLink(parser.parse(buffer.toString(), strUrlDoc, urlToCrawl.getHost(), date_modified, size));
                    }
                    links_queue.addToCrawledList(strUrlDoc);
                } catch (MalformedURLException e) {
                    jTextArea1.append(e.toString() + "1\n");
                    links_queue.addToCrawledList(strUrlDoc);
                    links_queue.addToErorPagesList(strUrlDoc);
                } catch (UnknownServiceException e) {
                    jTextArea1.append(e.toString() + "2\n");
                    links_queue.addToCrawledList(strUrlDoc);
                    links_queue.addToErorPagesList(strUrlDoc);
                } catch (IOException e) {
                    jTextArea1.append(e.toString() + "3\n");
                    e.printStackTrace();
                    links_queue.addToCrawledList(strUrlDoc);
                    links_queue.addToErorPagesList(strUrlDoc);
                } catch (Exception e) {
                    jTextArea1.append(e.toString() + "4\n");
                    e.printStackTrace();
                    links_queue.addToCrawledList(strUrlDoc);
                    links_queue.addToErorPagesList(strUrlDoc);
                }
            }
            try {
                jTextArea1.append("Optimizing index...\n");
                Parser.HtmlIndexer.optimizeIndex();
                jTextArea1.append("Saving links queue...\n");
                links_queue.saveList();
                jTextArea1.append("Finished.\n");
            } catch (Exception e) {
                System.out.println("Exception here " + e.toString());
            }
            jButton3.setEnabled(false);
            jButton1.setEnabled(true);
            jButton2.setEnabled(true);
            jButton4.setEnabled(true);
        }
