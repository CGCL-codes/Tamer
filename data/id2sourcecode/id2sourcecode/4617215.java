    public String uploadObject(Object object, String targetName) throws CommunicationException {
        String returnStr = new String();
        String requestString = new String();
        String boundary = "--8ah6j4h8das213--\n";
        HttpURLConnection connection = null;
        System.out.println("HTTPCommunication.uploadObject() - Object:" + object.getClass().getName() + " Targetname:" + targetName);
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=8ah6j4h8das213--");
            for (Iterator i = parameters.entrySet().iterator(); i.hasNext(); ) {
                java.util.Map.Entry e = (java.util.Map.Entry) i.next();
                requestString = requestString + boundary + "Content-Disposition: form-data; name=\"" + (String) e.getKey() + "\"\n\n" + (String) e.getValue() + "\n\n";
            }
            requestString += boundary + "Content-Disposition: form-data; " + "name=\"" + object.getClass().getName() + "\"; " + "filename=\"" + targetName + "\"\n" + "Content-Type: application/x-java-object\n\n";
            connection.connect();
            OutputStream out = connection.getOutputStream();
            out.write(requestString.getBytes("UTF-8"));
            GZIPOutputStream gzipOut = new GZIPOutputStream(out);
            ObjectOutput objectOut = new ObjectOutputStream(gzipOut);
            objectOut.writeObject(object);
            gzipOut.finish();
            objectOut.flush();
            out.write(("\n\n" + boundary + "\n\n").getBytes("UTF-8"));
            objectOut.close();
            System.out.println("HTTPCommunication.uploadObject() - Response:" + connection.getResponseCode() + " : " + connection.getResponseMessage());
            if (connection.getResponseCode() == connection.HTTP_OK) {
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                while (bufferedReader.ready()) {
                    returnStr += bufferedReader.readLine();
                }
                bufferedReader.close();
            } else {
                throw new CommunicationException(connection.getResponseMessage(), connection.getResponseCode());
            }
        } catch (java.net.ConnectException ce) {
            throw new CommunicationException("Cannot connect to " + urlString + ".\n" + "Server is not responding!", ce);
        } catch (java.net.MalformedURLException mfue) {
            throw new CommunicationException("Cannot connect to " + urlString + ".\n" + "Bad url string!", mfue);
        } catch (java.io.InterruptedIOException iioe) {
            this.parentWorkflow.getMenuButtonEventHandler().stopAutomaticRefresh();
            throw new CommunicationException("Communication is timeouted", iioe);
        } catch (java.io.IOException ioe) {
            throw new CommunicationException("Error while trying to communicate the server: \n" + ioe.getMessage(), ioe);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return returnStr;
    }
