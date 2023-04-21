    private Document fetchUrl(String url) {
        try {
            HttpURLConnection openConnection = (HttpURLConnection) new URL(url).openConnection();
            if (openConnection.getResponseCode() == 200) return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(openConnection.getInputStream()); else throw new BitlyException("Transport error! " + openConnection.getResponseCode() + " " + openConnection.getResponseMessage());
        } catch (IOException e) {
            throw new BitlyException("Transport I/O error!", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
