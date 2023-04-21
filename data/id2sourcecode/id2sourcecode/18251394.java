    public static int downloadURLToLocalCache(URL url, File file) {
        try {
            int trialCount = 0;
            while (trialCount < 5) {
                InputStream inputStream = null;
                try {
                    trialCount++;
                    final URLConnection openConnection = new SourceFactory().makeN3URLConnection(url);
                    inputStream = openConnection.getInputStream();
                    int returnVal = ReaderUtils.copyReader(new InputStreamReader(inputStream), new FileWriter(file));
                    return returnVal;
                } catch (java.net.SocketException se) {
                    System.err.println("IOManager.downloadURLToLocalCache(): " + se + "\n\tretry: " + url);
                } catch (java.net.SocketTimeoutException se) {
                    System.err.println("IOManager.downloadURLToLocalCache(): " + se + "\n\tretry: " + url);
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        } catch (final IOException e) {
            String message = "downloadURLToLocalCache: " + url + " ==> " + file;
            System.err.println(message);
            e.printStackTrace();
            System.err.println("downloadURLToLocalCache: throw new RuntimeException " + e.getMessage());
            throw new RuntimeException(message, e);
        }
        return -1;
    }
