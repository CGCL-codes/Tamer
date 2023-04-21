    @Override
    public void execute(ProcessBundle bundle) throws Exception {
        final String invActOrStatId = (String) bundle.getParams().get("Isurf_Inv_Actorstat_ID");
        System.out.println("Inventory activity or status id = " + invActOrStatId);
        InventoryActivityOrStatusMessageBuilder iaosbuilder = new InventoryActivityOrStatusMessageBuilder(invActOrStatId);
        int result = iaosbuilder.buildMessage();
        final OBError msg = new OBError();
        if (result == 0) {
            ISURFInventoryActivityOrStatus e = OBDal.getInstance().get(ISURFInventoryActivityOrStatus.class, invActOrStatId);
            e.setSendMessage(true);
            OBDal.getInstance().save(e);
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
                case ErrorTypes.INVENTORY_ITEMLOCINFO_NOTFOUND:
                    {
                        msg.setMessage("Inventory Item Location Info Not Found. Please specify at least one.");
                        break;
                    }
                case ErrorTypes.INVENTORY_ACTIVITYLINEITEM_NOTFOUND:
                    {
                        msg.setMessage("Inventory Activity Line Item Not Found. Please specify at least one.");
                        break;
                    }
                case ErrorTypes.INVENTORY_ACTIVITYQUANTITIYSPEC_NOTFOUND:
                    {
                        msg.setMessage("Inventory Activity Quantity Specification Not Found. Please specify at least one.");
                        break;
                    }
                case ErrorTypes.INVENTORY_STATUSLINEITEM_NOTFOUND:
                    {
                        msg.setMessage("Inventory Status Line Item Not Found. Please specify at least one.");
                        break;
                    }
                case ErrorTypes.INVENTORY_STATUSQUANTITYSPEC_NOTFOUND:
                    {
                        msg.setMessage("Inventory Status Quantity Specification Not Found. Please specify at least one.");
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
                case ErrorTypes.RECEIVERPRODUCT_NOT_FOUND:
                    {
                        msg.setMessage(" Error From Receiving Company: Product Not Found. Please Contact To The Company's Openbravo Administrator.");
                        break;
                    }
                case ErrorTypes.RECEIVER_CURRENCY_NOT_FOUND:
                    {
                        msg.setMessage(" Error From Receiving Company: Currency Not Found. Please Contact To The Company's Openbravo Administrator.");
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
                case ErrorTypes.WEBSERVICE_DEF_NOT_FOUND:
                    {
                        msg.setMessage("Web service definition for the specified business partner not found. Please define the web service in initial setup part.");
                        break;
                    }
                case ErrorTypes.WSDL_OPERATION_NOTFOUND:
                    {
                        msg.setMessage("In web service definition, the inventory activity or inventory status operation is not defined. Please define the operation.");
                        break;
                    }
                case ErrorTypes.PRODUCT_GTIN_NOTFOUND:
                    {
                        msg.setMessage("Specified Product needs UPC/EAN number.");
                        break;
                    }
            }
            bundle.setResult(msg);
        }
        System.out.println(result);
    }
