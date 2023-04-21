    private RemoteObject buildStream() throws RemoteException {
        RemoteObject returnValue = null;
        try {
            String streamQuery = StringUtils.join("SELECT post_id, actor_id, target_id, message,comments,likes FROM stream WHERE source_id in (SELECT target_id FROM connection WHERE source_id=\'", String.valueOf(getApi().users_getLoggedInUser()), "\')");
            FqlQueryResponse fqlQuery = (FqlQueryResponse) getApi().fql_query(streamQuery);
            JAXBContext newInstance = JAXBContext.newInstance(FqlQueryResponse.class);
            Marshaller createMarshaller = newInstance.createMarshaller();
            ByteArrayOutputStream stringOutputStream = new ByteArrayOutputStream();
            createMarshaller.marshal(fqlQuery, stringOutputStream);
            MessageDigest instance;
            try {
                instance = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RemoteException(StatusCreator.newStatus("Error initializing MD5", e));
            }
            instance.update(stringOutputStream.toByteArray());
            returnValue = InfomngmntFactory.eINSTANCE.createRemoteObject();
            returnValue.setId(IdFactory.createId());
            returnValue.setUrl(StringUtils.join("http://facebook/mystream"));
            returnValue.setName("My Stream");
            returnValue.setHash(asHex(instance.digest()));
            returnValue.setWrappedObject(fqlQuery);
        } catch (FacebookException e) {
            throw new RemoteException(StatusCreator.newStatus("Error getting stream", e));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
