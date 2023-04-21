    @Override
    public void execute(PaymentInfoMagcard payinfo) {
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("username=");
            sb.append(m_sCommerceID);
            sb.append("&password=");
            sb.append(m_sCommercePassword);
            sb.append("&amount=");
            NumberFormat formatter = new DecimalFormat("0000.00");
            String amount = formatter.format(Math.abs(payinfo.getTotal()));
            sb.append(URLEncoder.encode(amount.replace(',', '.'), "UTF-8"));
            if (payinfo.getTrack1(true) == null) {
                sb.append("&ccnumber=");
                sb.append(URLEncoder.encode(payinfo.getCardNumber(), "UTF-8"));
                sb.append("&ccexp=");
                sb.append(payinfo.getExpirationDate());
                String[] cc_name = payinfo.getHolderName().split(" ");
                sb.append("&firstname=");
                if (cc_name.length > 0) {
                    sb.append(URLEncoder.encode(cc_name[0], "UTF-8"));
                }
                sb.append("&lastname=");
                if (cc_name.length > 1) {
                    sb.append(URLEncoder.encode(cc_name[1], "UTF-8"));
                }
            } else {
                sb.append("&track_1=" + URLEncoder.encode(payinfo.getTrack1(true), "UTF-8"));
                sb.append("&track_2=" + URLEncoder.encode(payinfo.getTrack2(true), "UTF-8"));
            }
            if (payinfo.getTotal() > 0.0) {
                sb.append("&type=");
                sb.append(OPERATIONVALIDATE);
            } else {
                sb.append("&type=");
                sb.append(OPERATIONREFUND);
                sb.append("&transactionid=");
                sb.append(payinfo.getTransactionID());
            }
            URL url = new URL(ENDPOINTADDRESS);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String returned = in.readLine();
            payinfo.setReturnMessage(returned);
            in.close();
            if (returned == null) {
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Response empty.");
            } else {
                Map props = new HashMap();
                StringTokenizer tk = new StringTokenizer(returned, "?&");
                while (tk.hasMoreTokens()) {
                    String sToken = tk.nextToken();
                    int i = sToken.indexOf('=');
                    if (i >= 0) {
                        props.put(URLDecoder.decode(sToken.substring(0, i), "UTF-8"), URLDecoder.decode(sToken.substring(i + 1), "UTF-8"));
                    } else {
                        props.put(URLDecoder.decode(sToken, "UTF-8"), null);
                    }
                }
                if ("100".equals(props.get("response_code"))) {
                    payinfo.paymentOK((String) props.get("authcode"), (String) props.get("transactionid"), returned);
                } else {
                    payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), (String) props.get("responsetext"));
                }
            }
        } catch (UnsupportedEncodingException eUE) {
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eUE.getMessage());
        } catch (MalformedURLException eMURL) {
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eMURL.getMessage());
        } catch (IOException e) {
            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), e.getMessage());
        }
    }
