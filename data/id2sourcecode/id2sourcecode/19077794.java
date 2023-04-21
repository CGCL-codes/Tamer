    @Override
    public void run() {
        if (debug) System.out.println("downloader " + id + " gestartet");
        int count = 0;
        if (debug) System.out.println("Anzahl Jobs fuer Downloader " + id + ": " + jobs.size());
        this.runFlag = true;
        for (RSSFeed feed : jobs) {
            if (!runFlag) {
                break;
            }
            if (debug) System.out.println("id " + id + " count " + count);
            count++;
            try {
                URL url = new URL(feed.getUrl());
                URLConnection conn = url.openConnection();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] chunk = new byte[1024];
                int chunkSize;
                while ((chunkSize = is.read(chunk)) != -1) {
                    bos.write(chunk, 0, chunkSize);
                }
                String xmlString = bos.toString("UTF-8");
                if (xmlString.toLowerCase().contains("<?xml")) {
                    if (xmlString.toLowerCase().contains("encoding=\"iso-8859-1\"")) {
                        xmlString = bos.toString("ISO-8859-1");
                    }
                    feed.setXml(xmlString);
                    doneJobs.add(feed);
                } else {
                    errorHandler.receiveException(new RSSNetworkException(feed.getTitle() + "\n" + "Die URL (" + feed.getUrl() + ") verweist nicht auf eine XML-Datei!", false, true));
                }
                bos.flush();
                bos.close();
                is.close();
            } catch (SocketTimeoutException e) {
                errorHandler.receiveException(new RSSNetworkException(feed.getUsertitle() + "\nTimeout!" + "\nDie angegebene URL (" + feed.getUrl() + ") ist nicht erreichbar.", false, false));
            } catch (MalformedURLException e) {
                errorHandler.receiveException(new RSSNetworkException(feed.getUsertitle() + "\nFalsches URL-Format oder unbekanntes Uebertragungsprotokoll." + "\nBitte ueberpruefen sie die Feed-URL: " + feed.getUrl(), false, true));
            } catch (UnsupportedEncodingException e) {
                errorHandler.receiveException(new RSSNetworkException("Die angegebene Formatkodierung wird nicht unterstuetzt.", false, false));
            } catch (UnknownHostException e) {
                errorHandler.receiveException(new RSSNetworkException(feed.getUsertitle() + "\nDer angegebene Host (" + feed.getUrl() + ") konnte nicht gefunden werden." + "\nBitte ueberpruefen sie ihre Internetverbindung.", false, true));
            } catch (IOException e) {
                errorHandler.receiveException(new RSSNetworkException(feed.getUsertitle() + "\nEin-Ausgabefehler, wahrscheinlich wurde die Internetverbindung unterbrochen.", false, false));
            } catch (Exception e) {
                errorHandler.receiveException(new RSSNetworkException(feed.getUsertitle() + "\nEs ist ein unbekannter Fehler aufgetreten.", true, true));
            }
        }
        runFlag = false;
        terminated = true;
    }
