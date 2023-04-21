    @Override
    public void doEncode(AbstractMessage message, XMLStreamWriter writer) throws XMLStreamException {
        DialysisPatient dia = (DialysisPatient) message;
        if (dia == null) {
            System.out.println("WARNING: Object dialysis is null and cannot be encoded");
            return;
        }
        writer.writeStartElement(DialysisPatient.ID);
        writer.writeStartElement("id");
        writer.writeCharacters(String.valueOf(dia.getId()));
        writer.writeEndElement();
        if (dia.getPatient() != null) {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(Patient.ID);
            encoder.doEncode(dia.getPatient(), writer);
        }
        if (dia.getLocation() != null) {
            encoder = ProtocolCodecFactory.getDefault().getEncoder(Location.ID);
            encoder.doEncode(dia.getLocation(), writer);
        }
        writer.writeStartElement("fromStreet");
        writer.writeCharacters(dia.getFromStreet());
        writer.writeEndElement();
        writer.writeStartElement("fromCity");
        writer.writeCharacters(dia.getFromCity());
        writer.writeEndElement();
        if (dia.getToStreet() != null) {
            writer.writeStartElement("toStreet");
            writer.writeCharacters(dia.getToStreet());
            writer.writeEndElement();
        }
        if (dia.getToCity() != null) {
            writer.writeStartElement("toCity");
            writer.writeCharacters(dia.getToCity());
            writer.writeEndElement();
        }
        if (dia.getInsurance() != null) {
            writer.writeStartElement("insurance");
            writer.writeCharacters(dia.getInsurance());
            writer.writeEndElement();
        }
        if (dia.getKindOfTransport() != null) {
            writer.writeStartElement("kindOfTransport");
            writer.writeCharacters(dia.getKindOfTransport());
            writer.writeEndElement();
        }
        writer.writeStartElement("assistantPerson");
        writer.writeCharacters(String.valueOf(dia.isAssistantPerson()));
        writer.writeEndElement();
        writer.writeStartElement("stationary");
        writer.writeCharacters(String.valueOf(dia.isStationary()));
        writer.writeEndElement();
        if (dia.getPlannedStartOfTransport() > 0) {
            writer.writeStartElement("plannedStartOfTransport");
            writer.writeCharacters(Long.toString(dia.getPlannedStartOfTransport()));
            writer.writeEndElement();
        }
        if (dia.getPlannedTimeAtPatient() > 0) {
            writer.writeStartElement("plannedTimeAtPatient");
            writer.writeCharacters(Long.toString(dia.getPlannedTimeAtPatient()));
            writer.writeEndElement();
        }
        if (dia.getAppointmentTimeAtDialysis() > 0) {
            writer.writeStartElement("appointmentTimeAtDialysis");
            writer.writeCharacters(Long.toString(dia.getAppointmentTimeAtDialysis()));
            writer.writeEndElement();
        }
        if (dia.getPlannedStartForBackTransport() > 0) {
            writer.writeStartElement("plannedStartForBackTransport");
            writer.writeCharacters(Long.toString(dia.getPlannedStartForBackTransport()));
            writer.writeEndElement();
        }
        if (dia.getReadyTime() > 0) {
            writer.writeStartElement("readyTime");
            writer.writeCharacters(Long.toString(dia.getReadyTime()));
            writer.writeEndElement();
        }
        writer.writeStartElement("monday");
        writer.writeCharacters(String.valueOf(dia.isMonday()));
        writer.writeEndElement();
        writer.writeStartElement("tuesday");
        writer.writeCharacters(String.valueOf(dia.isTuesday()));
        writer.writeEndElement();
        writer.writeStartElement("wednesday");
        writer.writeCharacters(String.valueOf(dia.isWednesday()));
        writer.writeEndElement();
        writer.writeStartElement("thursday");
        writer.writeCharacters(String.valueOf(dia.isThursday()));
        writer.writeEndElement();
        writer.writeStartElement("friday");
        writer.writeCharacters(String.valueOf(dia.isFriday()));
        writer.writeEndElement();
        writer.writeStartElement("saturday");
        writer.writeCharacters(String.valueOf(dia.isSaturday()));
        writer.writeEndElement();
        writer.writeStartElement("sunday");
        writer.writeCharacters(String.valueOf(dia.isSunday()));
        writer.writeEndElement();
        writer.writeStartElement("lastTransportDate");
        writer.writeCharacters(String.valueOf(dia.getLastTransportDate()));
        writer.writeEndElement();
        writer.writeStartElement("lastBackTransportDate");
        writer.writeCharacters(String.valueOf(dia.getLastBackTransporDate()));
        writer.writeEndElement();
        writer.writeEndElement();
    }
