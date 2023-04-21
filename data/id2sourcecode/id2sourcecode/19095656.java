    public boolean transactionChangeStatusRequest(String orderCommand, String orderNumber, String orderAmount, String transactionId, String forceSettlement) {
        try {
            StringBuffer query = new StringBuffer();
            query.append("szSerialNumber=");
            query.append(this.getHtmlSerialNumber());
            query.append("&szDeveloperSerialNumber=");
            query.append(this.getDeveloperSerialNumber());
            query.append("&szOrderNumber=");
            query.append(orderNumber.trim());
            query.append("&szDesiredStatus=");
            query.append(orderCommand.trim());
            query.append("&szAmount=");
            query.append(orderAmount.trim());
            query.append("&szTransactionId=");
            query.append(transactionId.trim());
            query.append("&szForceSettlement=");
            query.append(forceSettlement.trim());
            URL url = new URL("https://" + this.SjTransactionChangeStatusRequestUrl);
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(query.toString());
            output.close();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            for (int c = in.read(); c != -1; c = in.read()) {
                System.out.print((char) c);
            }
            in.close();
            return true;
        } catch (Exception E) {
            System.out.println(E.getMessage());
            return false;
        } catch (Throwable T) {
            System.out.println(T.getMessage());
            return false;
        }
    }
