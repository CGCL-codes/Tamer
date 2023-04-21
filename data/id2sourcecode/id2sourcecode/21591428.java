    public tr.com.srdc.isurf.cpfr.gdssu.GetCollaborationPartnerResponse getCollaborationPartner(tr.com.srdc.isurf.cpfr.gdssu.GetCollaborationPartner getCollaborationPartner) {
        StringWriter searchResponseXML = new StringWriter();
        try {
            URL url = new URL("http://144.122.230.12:8080/iSurf/tcsu/searchRequest.xml");
            Scanner scan = new Scanner(url.openStream());
            while (scan.hasNextLine()) searchResponseXML.write(scan.nextLine() + "\n");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL Exception occured");
        } catch (IOException e) {
            System.out.println("IO Exception occured");
        }
        GetCollaborationPartnerResponse response = new GetCollaborationPartnerResponse();
        response.set_return(searchResponseXML.toString());
        return response;
    }
