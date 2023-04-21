    public synchronized void firePointEvent(PointEvent pe) {
        PointData data = pe.getPointData();
        if (pe.isRaw()) {
            if (data != null && itsTranslations != null) {
                for (int i = 0; i < itsTranslations.length; i++) {
                    try {
                        if (itsTranslations[i] != null) {
                            data = itsTranslations[i].translate(data);
                        }
                    } catch (Throwable e) {
                        theirLogger.error("(" + getFullName() + ") Error on Translation " + (i + 1) + "/" + itsTranslations.length + ": " + e);
                        data = null;
                    }
                    if (data == null) {
                        break;
                    }
                }
            }
            if (data != null && !data.getName().equals(getFullName())) {
                data = new PointData(data);
                data.setName(getFullName());
            }
            pe = new PointEvent(this, data, false);
        }
        if (data != null && data.isValid()) {
            evaluateAlarms(data);
            if (getEnabled() && itsOutputTransactions != null && itsOutputTransactions.length > 0) {
                for (int i = 0; i < itsOutputTransactions.length; i++) {
                    Transaction thistrans = itsOutputTransactions[i];
                    if (thistrans != null) {
                        ExternalSystem ds = ExternalSystem.getExternalSystem(thistrans.getChannel());
                        if (ds == null) {
                            theirLogger.warn("(" + getFullName() + ") No ExternalSystem for output Transaction channel " + thistrans.getChannel());
                        } else if (!ds.isConnected()) {
                            theirLogger.warn("(" + getFullName() + ") While writing output data: ExternalSystem " + thistrans.getChannel() + " is not connected");
                        } else {
                            try {
                                ds.putData(this, data);
                            } catch (Exception e) {
                                theirLogger.warn("(" + getFullName() + ") while writing output data, ExternalSystem " + ds.getName() + " threw exception \"" + e + "\"");
                            }
                        }
                    }
                }
            }
            if (itsArchiver != null && itsEnabled) {
                for (int i = 0; i < itsArchive.length; i++) {
                    if (itsArchive[i] != null && itsArchive[i].checkArchiveThis(data)) {
                        itsArchiver.archiveData(this, data);
                        break;
                    }
                }
            }
        }
        if (data != null) {
            PointBuffer.updateData(this, data);
        }
        distributeData(pe);
        if (itsPeriod > 0) {
            if (data != null && data.isValid()) {
                itsNextEpoch = data.getTimestamp().getValue() + itsPeriod;
            } else {
                itsNextEpoch = (new AbsTime()).getValue() + itsPeriod;
            }
        }
    }
