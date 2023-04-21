    public void saveSettings(boolean loadValuesFromForms) {
        if (DEBUG) System.out.println("DEBUG Settings.saveSettings(boolean " + loadValuesFromForms + ")");
        PersistentBox.releaseHeadersOfAllPesistentBoxes();
        if (loadValuesFromForms) {
            try {
                appearanceSettingsForm.updateValuesFromForm();
                mujMailSettingsForm.updateValuesFromForm();
                smtpSettingsForm.updateValuesFromForm();
                retrievingSettingsForm.updateValuesFromForm();
                otherSettingsForm.updateValuesFromForm();
                storingSettingsForm.updateValuesFromForm();
                pollingSettingsForm.updateValuesFromForm();
            } catch (Exception ex) {
                ex.printStackTrace();
                updateValuesToForms();
                mujMail.getAlert().setAlert(this, Menu.getMenuInstance(), Lang.get(Lang.ALRT_ST_SAVING) + Lang.get(Lang.FAILED), MyAlert.DEFAULT, AlertType.ERROR);
            }
        }
        System.out.println("deleteMailsWhenHeaderDBIsFull: " + deleteMailsWhenHeaderDBIsFull);
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore("SETTINGS", true);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(buffer);
            stream.writeLong(SETTINGSVERSION);
            stream.writeUTF(mujMailSrvAddr);
            stream.writeUTF(mujMailSrvPort);
            stream.writeUTF(mujMailSrvLogin);
            stream.writeUTF(mujMailSrvPasswd);
            stream.writeUTF(primaryEmail);
            stream.writeUTF(smtpServer);
            stream.writeBoolean(smtpSSL);
            stream.writeByte(smtpSSLType);
            stream.writeShort(smtpPort);
            stream.writeUTF(smtpAuthName);
            stream.writeUTF(smtpAuthPass);
            stream.writeInt(fontSize);
            stream.writeBoolean(downWholeMail);
            stream.writeBoolean(downOnlyNeverSeen);
            stream.writeBoolean(delMailFromServer);
            stream.writeBoolean(delOnExit);
            stream.writeBoolean(addToAddressbook);
            stream.writeBoolean(smallFontMailForm);
            stream.writeBoolean(replaceTabs);
            stream.writeBoolean(moveToTrash);
            stream.writeBoolean(safeMode);
            stream.writeBoolean(deleteMailsWhenHeaderDBIsFull);
            stream.writeBoolean(deleteMailsBodyWhenBodyDBIsFull);
            stream.writeBoolean(storeMailsInFS);
            stream.writeShort(maxMailsRetrieve);
            stream.writeShort(maxLinesRetrieve);
            stream.writeLong(maxSizeOfBodypart);
            stream.writeBoolean(pollPlaysSound);
            stream.writeBoolean(pollDownloadsMails);
            stream.writeInt(pollInvl);
            stream.writeInt(timeout);
            stream.writeInt(theBoxSortModes);
            stream.writeUTF(storeMailsDir);
            stream.writeInt(maxNumOfHeadersInHeapMemory);
            stream.writeUTF(signature);
            stream.writeUTF(password);
            stream.writeBoolean(threading);
            stream.flush();
            if (rs.getNumRecords() == 1) {
                rs.setRecord(1, buffer.toByteArray(), 0, buffer.size());
            } else {
                rs.addRecord(buffer.toByteArray(), 0, buffer.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            mujMail.getAlert().setAlert(this, Menu.getMenuInstance(), Lang.get(Lang.ALRT_ST_SAVING) + Lang.get(Lang.FAILED), MyAlert.DEFAULT, AlertType.ERROR);
            return;
        } finally {
            if (mujMail.getDisplay().getCurrent() != mujMail.getMailForm()) {
                mujMail.mainMenu();
            }
            if (rs != null) try {
                rs.closeRecordStore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
