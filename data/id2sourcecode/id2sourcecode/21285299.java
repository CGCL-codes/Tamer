    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
        VehicleDetail vehicle = (VehicleDetail) message;
        if (vehicle == null) {
            System.out.println("WARNING: Object vehicle is null and cannot be encoded");
            return;
        }
        writer.writeStartElement(VehicleDetail.ID);
        if (vehicle.getVehicleName() != null) {
            writer.writeStartElement("vehicleName");
            writer.writeCharacters(vehicle.getVehicleName());
            writer.writeEndElement();
        }
        if (vehicle.getVehicleType() != null) {
            writer.writeStartElement("vehicleType");
            writer.writeCharacters(vehicle.getVehicleType());
            writer.writeEndElement();
        }
        if (vehicle.getDriver() != null) {
            vehicle.getDriver().function = "driver";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
            encoder.doEncode(vehicle.getDriver(), writer);
        }
        if (vehicle.getFirstParamedic() != null) {
            vehicle.getFirstParamedic().function = "firstParamedic";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
            encoder.doEncode(vehicle.getFirstParamedic(), writer);
        }
        if (vehicle.getSecondParamedic() != null) {
            vehicle.getSecondParamedic().function = "secondParamedic";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(StaffMember.ID);
            encoder.doEncode(vehicle.getSecondParamedic(), writer);
        }
        if (vehicle.getMobilePhone() != null) {
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(MobilePhoneDetail.ID);
            encoder.doEncode(vehicle.getMobilePhone(), writer);
        }
        if (vehicle.getVehicleNotes() != null) {
            writer.writeStartElement("vehicleNotes");
            writer.writeCharacters(vehicle.getVehicleNotes());
            writer.writeEndElement();
        }
        if (vehicle.getLastDestinationFree() != null) {
            writer.writeStartElement("lastDestinationFree");
            writer.writeCharacters(vehicle.getLastDestinationFree());
            writer.writeEndElement();
        }
        if (vehicle.getCurrentStation() != null) {
            vehicle.getCurrentStation().type = "current";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
            encoder.doEncode(vehicle.getCurrentStation(), writer);
        }
        if (vehicle.getBasicStation() != null) {
            vehicle.getBasicStation().type = "basic";
            MessageEncoder encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
            encoder.doEncode(vehicle.getBasicStation(), writer);
        }
        writer.writeStartElement("readyForAction");
        writer.writeCharacters(String.valueOf(vehicle.isReadyForAction()));
        writer.writeEndElement();
        writer.writeStartElement("outOfOrder");
        writer.writeCharacters(String.valueOf(vehicle.isOutOfOrder()));
        writer.writeEndElement();
        if (vehicle.getTransportStatus() > 0) {
            writer.writeStartElement("transportStatus");
            writer.writeCharacters(String.valueOf(vehicle.getTransportStatus()));
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
