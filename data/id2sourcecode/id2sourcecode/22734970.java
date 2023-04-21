    public void writePjson(PjsonWriteUtil writer) {
        writer.writeGUID(guid);
        writer.writeByteArray(json);
        writer.writeBoolean(readOnly);
        writer.writeGUID(apisession);
        writer.writeInt(serial);
        writer.writeGUID(widgetServerName);
        writer.writeGUID(saveAckGuid);
        writer.writeGUID(entity);
        writer.writeInt(expected);
        writer.writeGenericList(managers);
        writer.writeGUID(widget);
        writer.writeEntityStub(user);
    }
