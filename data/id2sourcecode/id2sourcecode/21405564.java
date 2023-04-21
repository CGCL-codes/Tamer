    public void execute(PaymentInfoMagcard payinfo) {
        StringBuffer sb = new StringBuffer();
        String currency = "978";
        String xml = "";
        if (m_sCurrency.equals("USD")) {
            currency = "840";
        } else if (m_sCurrency.equals("GPD")) {
            currency = "826";
        }
        NumberFormat nf = new DecimalFormat("00");
        String amount = nf.format(Math.abs(payinfo.getTotal()) * 100);
        String orderid = createOrderId();
        try {
            if (payinfo.getTotal() > 0.0) {
                String firma = amount + orderid + sMerchantCode + currency + payinfo.getCardNumber() + SALE + sCommerceSign;
                xml = "<DATOSENTRADA>" + "<DS_Version>0.1</DS_Version>" + "<DS_MERCHANT_AMOUNT>" + amount + "</DS_MERCHANT_AMOUNT>" + "<DS_MERCHANT_CURRENCY>" + currency + "</DS_MERCHANT_CURRENCY>" + "<DS_MERCHANT_ORDER>" + orderid + "</DS_MERCHANT_ORDER>" + "<DS_MERCHANT_MERCHANTCODE>" + sMerchantCode + "</DS_MERCHANT_MERCHANTCODE>" + "<DS_MERCHANT_MERCHANTURL></DS_MERCHANT_MERCHANTURL>" + "<DS_MERCHANT_MERCHANTSIGNATURE>" + getSHA1(firma) + "</DS_MERCHANT_MERCHANTSIGNATURE>" + "<DS_MERCHANT_TERMINAL>" + sTerminal + "</DS_MERCHANT_TERMINAL>" + "<DS_MERCHANT_TRANSACTIONTYPE>" + SALE + "</DS_MERCHANT_TRANSACTIONTYPE>" + "<DS_MERCHANT_PAN>" + payinfo.getCardNumber() + "</DS_MERCHANT_PAN>" + "<DS_MERCHANT_EXPIRYDATE>" + payinfo.getExpirationDate() + "</DS_MERCHANT_EXPIRYDATE>" + "</DATOSENTRADA>";
            } else {
                String firma = amount + payinfo.getTransactionID() + sMerchantCode + currency + REFUND + sCommerceSign;
                xml = "<DATOSENTRADA>" + "<DS_Version>0.1</DS_Version>" + "<DS_MERCHANT_AMOUNT>" + amount + "</DS_MERCHANT_AMOUNT>" + "<DS_MERCHANT_CURRENCY>" + currency + "</DS_MERCHANT_CURRENCY>" + "<DS_MERCHANT_ORDER>" + payinfo.getTransactionID() + "</DS_MERCHANT_ORDER>" + "<DS_MERCHANT_MERCHANTCODE>" + sMerchantCode + "</DS_MERCHANT_MERCHANTCODE>" + "<DS_MERCHANT_MERCHANTURL></DS_MERCHANT_MERCHANTURL>" + "<DS_MERCHANT_MERCHANTSIGNATURE>" + getSHA1(firma) + "</DS_MERCHANT_MERCHANTSIGNATURE>" + "<DS_MERCHANT_TERMINAL>" + sTerminal + "</DS_MERCHANT_TERMINAL>" + "<DS_MERCHANT_TRANSACTIONTYPE>" + REFUND + "</DS_MERCHANT_TRANSACTIONTYPE>" + "</DATOSENTRADA>";
            }
            sb.append("entrada=" + URLEncoder.encode(xml, "UTF-8"));
            URL url = new URL(ENDPOINTADDRESS);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String sReturned = in.readLine();
            in.close();
            if (sReturned == null) {
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Response empty.");
            } else {
                LaCaixaParser lpp = new LaCaixaParser(sReturned);
                Map props = lpp.splitXML();
                if (lpp.getResult().equals(LocalRes.getIntString("button.ok"))) {
                    if (SALEAPPROVED.equals(props.get("Ds_Response")) || REFUNDAPPROVED.equals(props.get("Ds_Response"))) {
                        payinfo.paymentOK((String) props.get("Ds_AuthorisationCode"), (String) props.get("Ds_Order"), sReturned);
                    } else {
                        String sCode = (String) props.get("Ds_Response");
                        if ("0101".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymentnotauthorised"), "Card date expired");
                        } else if ("0102".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Tarjeta en excepción transitoria o bajo sospecha de fraude.");
                        } else if ("0104".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Operación no permitida para esa tarjeta o terminal.");
                        } else if ("0116".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Disponible insuficiente.");
                        } else if ("0118".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Tarjeta no registrada.");
                        } else if ("0129".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "CVV2 security code invalid. Amount not supplied or invalid.");
                        } else if ("0180".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Tarjeta ajena al servicio.");
                        } else if ("0184".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Cardholder authentication error.");
                        } else if ("0190".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Denegation of service without reason.");
                        } else if ("0191".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Expiry date invalid.");
                        } else if ("0202".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Tarjeta en excepción transitoria o bajo sospecha de fraude con retirada de tarjeta.");
                        } else if ("0904".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Comercio no registrado en FUC.");
                        } else if (("9912".equals(sCode)) || ("912".equals(sCode))) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Emisor no disponible.");
                        } else {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterrorunknown"), "");
                        }
                        sCode = (String) props.get("CODIGO");
                        if ("SIS0054".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymentnotauthorised"), "Pedido repetido.");
                        } else if ("SIS0078".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Método de pago no disponible para su tarjeta.");
                        } else if ("SIS0093".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Tarjeta no válida.");
                        } else if ("SIS0094".equals(sCode)) {
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), "Error en la llamada al MPI sin controlar.");
                        }
                    }
                } else {
                    payinfo.paymentError(lpp.getResult(), "");
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
