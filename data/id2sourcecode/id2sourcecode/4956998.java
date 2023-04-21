    public static AbstractAction getFedoraAction(osid.repository.Record record, osid.repository.Repository repository) throws osid.repository.RepositoryException {
        final Repository mRepository = (Repository) repository;
        final Record mRecord = (Record) record;
        try {
            AbstractAction fedoraAction = new AbstractAction(record.getId().getIdString()) {

                public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                    try {
                        osid.shared.Id id = new PID(getFedoraProperty(mRepository, "DisseminationURLInfoPartId"));
                        osid.repository.PartIterator partIterator = mRecord.getParts();
                        while (partIterator.hasNextPart()) {
                            osid.repository.Part part = partIterator.nextPart();
                            {
                                String fedoraUrl = part.getValue().toString();
                                URL url = new URL(fedoraUrl);
                                URLConnection connection = url.openConnection();
                                System.out.println("FEDORA ACTION: Content-type:" + connection.getContentType() + " for url :" + fedoraUrl);
                                tufts.Util.openURL(fedoraUrl);
                                break;
                            }
                        }
                    } catch (Throwable t) {
                    }
                }
            };
            return fedoraAction;
        } catch (Throwable t) {
            throw new osid.repository.RepositoryException("FedoraUtils.getFedoraAction " + t.getMessage());
        }
    }
