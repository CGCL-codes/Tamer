    @Override
    public void run() {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Backupthread started");
            }
            final ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(_file));
            final ByteArrayOutputStream ost = new ByteArrayOutputStream();
            final XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(ost, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
            writer.writeStartElement("mp3db");
            writer.writeAttribute("version", "5");
            int itemCount = 0;
            try {
                itemCount += getItemCount("mediafile");
                itemCount += getItemCount("album");
                itemCount += getItemCount("cd");
                itemCount += _version < 3 ? getItemCount("cover") : getItemCount("coveritem");
                fireStatusEvent(new StatusEvent(this, StatusEventType.MAX_VALUE, itemCount));
            } catch (final Exception e) {
                LOG.error("Error getting size", e);
                fireStatusEvent(new StatusEvent(this, StatusEventType.MAX_VALUE, -1));
            }
            int cdCounter = 0;
            int mediafileCounter = 0;
            int albumCounter = 0;
            int coveritemCounter = 0;
            int counter = 0;
            final List data = getCdsOrderById();
            if (data.size() > 0) {
                final Map<Integer, Integer> albums = new HashMap<Integer, Integer>();
                final Iterator it = data.iterator();
                while (it.hasNext() && !_break) {
                    final Vector vdCd = (Vector) it.next();
                    final Integer cdId = Integer.valueOf(cdCounter++);
                    writer.writeStartElement(TypeConstants.XML_CD);
                    exportCd(writer, vdCd, cdId);
                    fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_VALUE, ++counter));
                    final List files = getMediafileByCd(((Number) vdCd.get(0)).intValue());
                    final Iterator mfit = files.iterator();
                    while (mfit.hasNext() && !_break) {
                        final Vector mf = (Vector) mfit.next();
                        final Integer mfId = Integer.valueOf(mediafileCounter++);
                        writer.writeStartElement(TypeConstants.XML_MEDIAFILE);
                        exportMediafile(writer, mf, mfId);
                        fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_VALUE, ++counter));
                        final int albumId = ((Number) mf.get(3)).intValue();
                        final Vector a = getAlbumById(albumId);
                        if (a != null) {
                            Integer inte;
                            if (albums.containsKey(a.get(0))) {
                                inte = albums.get(a.get(0));
                                writeLink(writer, TypeConstants.XML_ALBUM, inte);
                            } else {
                                inte = Integer.valueOf(albumCounter++);
                                writer.writeStartElement(TypeConstants.XML_ALBUM);
                                exportAlbum(writer, a, inte);
                                fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_VALUE, ++counter));
                                albums.put(albumId, inte);
                                if (!_break) {
                                    switch(_version) {
                                        case 1:
                                        case 2:
                                            final int cid = ((Number) a.get(4)).intValue();
                                            if (cid > 0) {
                                                final Vector covers = getCoversById(cid);
                                                for (int i = 1; i < 5; i++) {
                                                    final byte[] coverData = (byte[]) covers.get(i);
                                                    if (coverData != null) {
                                                        final Integer coveritemId = Integer.valueOf(coveritemCounter++);
                                                        String type;
                                                        switch(i) {
                                                            case 1:
                                                                type = "Front";
                                                                break;
                                                            case 2:
                                                                type = "Back";
                                                                break;
                                                            case 3:
                                                                type = "Inlay";
                                                                break;
                                                            case 4:
                                                                type = "Cd";
                                                                break;
                                                            case 5:
                                                                type = "Other";
                                                                break;
                                                            default:
                                                                type = "Unknown";
                                                                break;
                                                        }
                                                        exportCoveritem(writer, zOut, type, coverData, coveritemId);
                                                    }
                                                }
                                            }
                                            fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_VALUE, ++counter));
                                            break;
                                        case 3:
                                        case 4:
                                            final List covers = getCoveritemByAlbum(albumId);
                                            final Iterator coit = covers.iterator();
                                            while (coit.hasNext() && !_break) {
                                                final Integer coveritemId = Integer.valueOf(coveritemCounter++);
                                                final Vector coveritem = (Vector) coit.next();
                                                exportCoveritem(writer, zOut, String.valueOf(coveritem.get(2)), (byte[]) coveritem.get(3), coveritemId);
                                                fireStatusEvent(new StatusEvent(this, StatusEventType.NEW_VALUE, ++counter));
                                            }
                                    }
                                }
                                writer.writeEndElement();
                            }
                        }
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                    writer.flush();
                    it.remove();
                    GenericDAO.getEntityManager().close();
                }
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
            ost.flush();
            ost.close();
            if (_break) {
                zOut.close();
                _file.delete();
            } else {
                zOut.putNextEntry(new ZipEntry("mp3.xml"));
                IOUtils.write(ost.toByteArray(), zOut);
                zOut.close();
            }
            fireStatusEvent(new StatusEvent(this, StatusEventType.FINISH));
        } catch (final Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error backup database", e);
            }
            fireStatusEvent(new StatusEvent(this, e, ""));
        }
    }
