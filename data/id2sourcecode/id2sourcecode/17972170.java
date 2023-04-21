    public String addNamespace(Element root) throws Exception {
        NodeList children = root.getChildNodes();
        int len = children.getLength();
        Node node = children.item(0);
        String name = getAttribute(node, NAME);
        String id = getAttribute(node, ID);
        String code = getAttribute(node, CODE);
        String type = getAttribute(node, TYPE);
        String authority_id = getAttribute(node, AUTHORITY_ID);
        String referenced_by = getAttribute(node, REFERENCED_BY);
        String local = getAttribute(node, "local");
        String writable = getAttribute(node, "writable");
        String semanticType = getAttribute(node, "semanticType");
        name = DTSUtil.checkValue("Namespace Name", name, DTSDataLimits.LEN_NAME);
        code = DTSUtil.checkValue("Namespace Code", code, DTSDataLimits.LEN_CODE);
        insertSt.setInt(1, Integer.parseInt(id));
        insertSt.setString(2, name);
        insertSt.setString(3, code);
        insertSt.setString(4, referenced_by);
        insertSt.setString(5, authority_id);
        insertSt.setString(6, local);
        insertSt.setString(7, writable);
        insertSt.setString(8, semanticType);
        insertSt.setString(9, type);
        conn.setAutoCommit(false);
        try {
            int result = insertSt.executeUpdate();
            if (result != 1) {
                conn.rollback();
                return getFalseResult();
            }
            if (type.equals("E")) {
                if (!local.equals("T")) {
                    throw new DTSValidationException("Namespace of Ontylog Extension type should be a local.");
                }
                String linkedNSId = getAttribute(node, "linkedNSId");
                if (linkedNSId == null) {
                    throw new DTSValidationException("Linked namespace data is not found." + "Linked namespace is required to create an Ontylog Extension local namespace.");
                }
                boolean linkageAdded = addNamespaceLinkage(Integer.parseInt(id), Integer.parseInt(linkedNSId));
                if (!linkageAdded) {
                    conn.rollback();
                    return getFalseResult();
                }
            }
            addCache(new Integer(id), name, local, writable, type);
            conn.commit();
            return getTrueResult();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
