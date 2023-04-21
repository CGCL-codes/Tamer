        @Override
        public void write(MojasiWriter w, Object obj1) throws MojasiException {
            SaveEntityTransaction me = (SaveEntityTransaction) obj1;
            super.write(w, obj1);
            if ((w.mask & 0xffff) != 0) w.writeByteArray(json_moj, me.json);
            if ((w.mask & 0xffff) != 0) w.writeBool(readOnly_moj, me.readOnly);
            if ((w.mask & 0xffff) != 0) w.writeGenericObject(apisession_moj, me.apisession);
            if ((w.mask & 0xffff) != 0) w.writeInt(serial_moj, me.serial);
            if ((w.mask & 0xffff) != 0) w.writeGenericObject(widgetServerName_moj, me.widgetServerName);
            if ((w.mask & 0xffff) != 0) w.writeGenericObject(saveAckGuid_moj, me.saveAckGuid);
        }
