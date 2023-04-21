    private boolean canAccessProject(Path absPath, int permissions) throws Exception {
        Node project = (Node) systemSession.getItem(resolver.getJCRPath(absPath.subPath(0, 3)));
        if (isUser) {
            Node user = (Node) systemSession.getItem("/users/" + session.getUserID());
            ArrayList<Node> subordinates = new ArrayList<Node>();
            @SuppressWarnings("deprecation") Query query = systemSession.getWorkspace().getQueryManager().createQuery("/jcr:root/users/element(*, user)[\n" + "  @supervisors = '" + user.getIdentifier().replaceAll("'", "''") + "'\n" + "]\n" + "order by @lastModified descending", Query.XPATH);
            NodeIterator results = query.execute().getNodes();
            while (results.hasNext()) {
                subordinates.add(results.nextNode());
            }
            if (userInAccessors(user, project.getNode("managers")) || subordinateInAccessors(subordinates, project.getNode("managers"))) {
                return true;
            }
            if (absPath.getLength() == 3) {
                if ((permissions & Permission.REMOVE_NODE) != 0x00) {
                    return false;
                }
            }
            if (absPath.getLength() == 4) {
                Name name = absPath.getNameElement().getName();
                if (name.equals(resolver.getQName("published")) || name.equals(resolver.getQName("managers")) || name.equals(resolver.getQName("writers")) || name.equals(resolver.getQName("readers"))) {
                    return permissions == Permission.READ;
                }
            }
            if (userInAccessors(user, project.getNode("writers")) || subordinateInAccessors(subordinates, project.getNode("writers"))) {
                return true;
            }
            if (userInAccessors(user, project.getNode("readers")) || subordinateInAccessors(subordinates, project.getNode("readers"))) {
                if (project.getName().startsWith("filemonitor-") && permissions == Permission.REMOVE_NODE) {
                    return true;
                }
                return permissions == Permission.READ;
            }
        }
        if (project.getProperty("published").getBoolean()) {
            if (absPath.getLength() <= 4) {
                return permissions == Permission.READ;
            } else if (absPath.getElements()[3].getName().equals(resolver.getQName("experiments"))) {
                Item item = systemSession.getItem(resolver.getJCRPath(absPath.subPath(0, 5)));
                if (!item.isNode() || ((Node) item).getProperty("published").getBoolean()) {
                    return permissions == Permission.READ;
                }
            }
        }
        return false;
    }
