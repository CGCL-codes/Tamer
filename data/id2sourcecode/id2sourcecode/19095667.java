    public boolean approveTransaction(SjTransactionInfo sjTransactionInfo, SjTransactionResponse sjTransactionResponse) {
        try {
            String transactionQuery = createTransactionQuery(sjTransactionInfo);
            URL url = new URL("https://" + this.getSjTransactionAuthorizeUrl());
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(transactionQuery);
            output.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            response = response + "#" + in.readLine();
            in.close();
            sjTransactionResponse.parse(response);
            return sjTransactionResponse.getSjIsApproved().equals("1");
        } catch (Exception ex) {
            System.err.println("Exception occured in skipjack webauthorize function");
            ex.printStackTrace(System.err);
            sjTransactionResponse.setDeclineReason("Unable to communicate with Credit Card Processor, try again later");
            return false;
        } catch (Throwable t) {
            System.err.println("Throwing error from skipjack webauthorize function");
            t.printStackTrace(System.err);
            sjTransactionResponse.setDeclineReason("Unable to communicate with Credit Card Processor, try again later");
            return false;
        }
    }
