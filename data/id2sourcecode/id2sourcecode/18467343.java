    public static void main(String[] args) throws Exception {
        DrupalService service = new DrupalXmlRpcService("localhost", "3cb91efcda4fd1d338aa07b611a7099a", "http://localhost/drupal/?q=services/xmlrpc");
        service.connect();
        service.login("root", "root");
        DrupalNode node = new DrupalNode();
        node.setType(DrupalNode.TYPE_STORY);
        node.setTitle("HEllo WORLD");
        node.setNid(1);
        DrupalNode retNode = service.nodeGet(node);
        System.out.println("retNode: " + retNode.toString());
        System.out.println("done");
    }
