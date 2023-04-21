    @Override
    public void execute(ProcessBundle bundle) throws Exception {
        final String productActivityId = (String) bundle.getParams().get("Isurf_Productactivity_ID");
        System.out.println(productActivityId);
        ProductActivityMessageBuilder pabuilder = new ProductActivityMessageBuilder(productActivityId);
        int result = pabuilder.buildMessage();
        final OBError msg = new OBError();
        System.out.println(result);
        if (result == 0) {
            ISURFProductActivity e = OBDal.getInstance().get(ISURFProductActivity.class, productActivityId);
            e.setSendMessage(true);
            msg.setType("Success");
            msg.setTitle("Done");
            msg.setMessage("Process finished correctly");
            bundle.setResult(msg);
        } else {
            msg.setType("Error");
            msg.setTitle("Error");
            switch(result) {
                case ErrorTypes.BPARTNERLOC_GLN_NOTFOUND:
                    {
                        msg.setMessage("Business Partner Global Location Number Not Found.");
                        break;
                    }
                case ErrorTypes.BPARTNERLOC_NOTFOUND:
                    {
                        msg.setMessage("Business Partner Location Not Found.");
                        break;
                    }
                case ErrorTypes.DATAITEM_NOTFOUND:
                    {
                        msg.setMessage("Please Specify at Least One Forecast Data Item.");
                        break;
                    }
                case ErrorTypes.ORGINFO_GLN_NOTFOUND:
                    {
                        msg.setMessage("Organization Global Location Number Not Found.");
                        break;
                    }
                case ErrorTypes.ORGINFO_NOTFOUND:
                    {
                        msg.setMessage("Organization Location Not Found.");
                        break;
                    }
                case ErrorTypes.PRODUCT_ORGINFO_GLN_NOTFOUND:
                    {
                        msg.setMessage("Product Organization Global Location Number Not Found.");
                        break;
                    }
                case ErrorTypes.PRODUCT_ORGINFO_NOTFOUND:
                    {
                        msg.setMessage("Product Organization Location Not Found.");
                        break;
                    }
                case ErrorTypes.PRODUCT_STORAGEBIN_NOTFOUND:
                    {
                        msg.setMessage("Product Storage Bin Information Not Found.");
                        break;
                    }
                case ErrorTypes.PRODUCT_STORAGEBIN_WH_GLN_NOTFOUND:
                    {
                        msg.setMessage("Warehouse Global Location Number Not Found.");
                        break;
                    }
                case ErrorTypes.RECEIVERORG_NOT_FOUND:
                    {
                        msg.setMessage("Error From Receiving Company: Organization Not Found.");
                        break;
                    }
                case ErrorTypes.RECEIVERBPARTNER_NOT_FOUND:
                    {
                        msg.setMessage("Error From Receiving Company: No Such Business Partner. Please Contact To The Company's Openbravo Administrator. ");
                        break;
                    }
                case ErrorTypes.RECEIVERBPARTNER_STORAGE_NOTFOUND:
                    {
                        msg.setMessage("Error From Receiving Company: Business Partner Storage Not Found. Please Contact To The Company's Openbravo Administrator.");
                        break;
                    }
                case ErrorTypes.RECEIVERPRODUCT_NOT_FOUND:
                    {
                        msg.setMessage(" Error From Receiving Company: Product Not Found. Please Contact To The Company's Openbravo Administrator.");
                        break;
                    }
                case ErrorTypes.RECEIVER_UOM_NOTFOUND:
                    {
                        msg.setMessage(" Error From Receiving Company: UOM Not Found. Please Contact To The Company's Openbravo Administrator.");
                        break;
                    }
                case ErrorTypes.GENERIC:
                    {
                        msg.setMessage("Unexpected error occured, please check your message and send again!");
                        break;
                    }
                case ErrorTypes.NETWORK_ERROR:
                    {
                        msg.setMessage("An error occured while connecting to network, please check your Openbravo's " + "network and web service configuration!");
                        break;
                    }
                case ErrorTypes.PRODUCT_GTIN_NOTFOUND:
                    {
                        msg.setMessage("Specified Product needs UPC/EAN number.");
                        break;
                    }
                case ErrorTypes.WEBSERVICE_DEF_NOT_FOUND:
                    {
                        msg.setMessage("Web service definition for the specified business partner not found. Please define the web service in initial setup part.");
                        break;
                    }
                case ErrorTypes.WSDL_OPERATION_NOTFOUND:
                    {
                        msg.setMessage("In web service definition, the product activity operation is not defined. Please define the operation.");
                        break;
                    }
            }
            bundle.setResult(msg);
        }
    }
