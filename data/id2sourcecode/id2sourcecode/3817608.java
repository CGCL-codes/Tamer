        public boolean process(Request req, ResponseGroup rg) throws EntityPersistenceException, StaleDataException, InvalidOperationException {
            if (!checkRequest(req)) {
                throw new InvalidOperationException("Invalid update operation.");
            }
            UpdateRequest r = (UpdateRequest) req;
            List<Feature> allFeatures = DaoUtil.getFeatureDao().getAll(r.getModelId());
            List<Feature2> list1 = new ArrayList<Feature2>();
            if (allFeatures != null) {
                for (Feature f : allFeatures) {
                    Feature2 f2 = new Feature2();
                    f.transfer(f2);
                    list1.add(f2);
                }
            }
            List<Relationship> allRelation = DaoUtil.getRelationshipDao().getAll(r.getModelId());
            List<BinaryRelation2> list2 = new ArrayList<BinaryRelation2>();
            if (allRelation != null) {
                for (Relationship rel : allRelation) {
                    if (isBinary(rel.getType())) {
                        BinaryRelation2 r2 = new BinaryRelation2();
                        ((BinaryRelationship) rel).transfer(r2);
                        list2.add(r2);
                    }
                }
            }
            Model m = DaoUtil.getModelDao().getById(r.getModelId(), false);
            List<Attribute2> list3 = new ArrayList<Attribute2>();
            for (Map.Entry<String, Attribute> e : m.getFeatureAttrs().entrySet()) {
                list3.add(EntityUtil.transferFromAttr(e.getValue()));
            }
            UpdateResponse response = new UpdateResponse(r);
            response.setFeatures(list1);
            response.setBinaries(list2);
            response.setAttrs(list3);
            response.setName(Resources.RSP_SUCCESS);
            rg.setBack(response);
            return true;
        }
