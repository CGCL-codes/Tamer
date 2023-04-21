                public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                    try {
                        org.osid.shared.Id id = new PID(getFedoraProperty(mRepository, "DisseminationURLInfoPartId"));
                        org.osid.repository.PartIterator partIterator = mRecord.getParts();
                        while (partIterator.hasNextPart()) {
                            org.osid.repository.Part part = partIterator.nextPart();
                            String fedoraUrl = part.getValue().toString();
                            URL url = new URL(fedoraUrl);
                            URLConnection connection = url.openConnection();
                            tufts.Util.openURL(fedoraUrl);
                            break;
                        }
                    } catch (Throwable t) {
                    }
                }
