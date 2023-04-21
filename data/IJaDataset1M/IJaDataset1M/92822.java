package de.fmannan.addbook.domainmodel;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import de.fmannan.addbook.domainmodel.contactdata.OfficeContactData;
import de.fmannan.addbook.domainmodel.contactdata.PrivateContactData;
import de.fmannan.addbook.persistence.xmlwriter.XMLWriter;

/**
 * @author fmannan
 * 
 * Represents a contact of an addressbook including name, address and
 * group-belongings.
 * 
 */
public class Contact implements IContact {

    private static final Logger log = Logger.getLogger(Contact.class);

    private PrivateContactData privateData;

    private OfficeContactData officeData;

    private String firstName;

    private String lastName;

    private Group parent;

    private boolean isNewlyCreated;

    public Contact() {
        this.firstName = "";
        this.lastName = "";
        isNewlyCreated = true;
        privateData = new PrivateContactData();
        officeData = new OfficeContactData();
    }

    public Contact(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        isNewlyCreated = true;
        privateData = new PrivateContactData();
        officeData = new OfficeContactData();
    }

    @Override
    public Object[] getChildren() {
        return new Object[0];
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Object getParent() {
        return parent;
    }

    @Override
    public void setParent(IContact parent) {
        this.parent = (Group) parent;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    public String toString() {
        return firstName + " " + lastName;
    }

    public boolean isNewlyCreated() {
        return isNewlyCreated;
    }

    public void setNewlyCreated(boolean isNewlyCreated) {
        this.isNewlyCreated = isNewlyCreated;
    }

    @Override
    public void writeToXML(XMLWriter out) throws IOException {
        log.debug("Writing Contact " + this.firstName + " " + this.lastName + " to XML");
        out.writeAttribute("firstname", this.firstName);
        out.writeAttribute("lastname", this.lastName);
        out.writeAttribute("newlycreated", String.valueOf(this.isNewlyCreated));
        out.writeStartElement("private");
        out.writeAttribute("birthdate", this.privateData.getBirthdate());
        out.writeAttribute("mobile", String.valueOf(this.privateData.getMobilePhoneNumber()));
        out.writeAttribute("phone", String.valueOf(this.privateData.getPhoneNumber()));
        out.writeAttribute("street", this.privateData.getStreet());
        out.writeAttribute("postalcode", String.valueOf(this.privateData.getPostalCode()));
        out.writeAttribute("city", this.privateData.getCity());
        out.writeAttribute("country", this.privateData.getCountry());
        out.writeAttribute("website", this.privateData.getWebsite());
        out.writeAttribute("email", this.privateData.getEmail());
        out.writeAttribute("remark", this.privateData.getRemark());
        out.writeEndElement();
        out.writeStartElement("office");
        out.writeAttribute("company", this.officeData.getCompanyName());
        out.writeAttribute("department", this.officeData.getDepartment());
        out.writeAttribute("occupation", this.officeData.getOccupation());
        out.writeAttribute("mobile", String.valueOf(this.officeData.getMobilePhoneNumber()));
        out.writeAttribute("phone", String.valueOf(this.privateData.getPhoneNumber()));
        out.writeAttribute("street", this.officeData.getStreet());
        out.writeAttribute("postalcode", String.valueOf(this.officeData.getPostalCode()));
        out.writeAttribute("city", this.officeData.getCity());
        out.writeAttribute("country", this.officeData.getCountry());
        out.writeAttribute("website", this.officeData.getWebsite());
        out.writeAttribute("email", this.officeData.getEmail());
        out.writeEndElement();
    }

    public PrivateContactData getPrivateData() {
        return privateData;
    }

    public OfficeContactData getOfficeData() {
        return officeData;
    }

    /**
	 * Builds a IContact item from XML Code.
	 * 
	 * @param node
	 *            the current node
	 * @return
	 */
    public static Contact buildFromXML(Node node) {
        Contact contact = new Contact();
        contact.setFirstName(node.getAttributes().getNamedItem("firstname").getNodeValue());
        contact.setLastName(node.getAttributes().getNamedItem("lastname").getNodeValue());
        contact.setNewlyCreated(Boolean.valueOf(node.getAttributes().getNamedItem("newlycreated").getNodeValue()));
        log.debug("Building Contact from XML: " + contact.getName());
        NodeList data = node.getChildNodes();
        log.debug("Number of Children of Contact: " + data.getLength());
        Node privData = data.item(0);
        log.debug("Private Data: " + privData.toString());
        contact.getPrivateData().setBirthdate((privData.getAttributes().getNamedItem("birthdate").getNodeValue()));
        contact.getPrivateData().setMobilePhoneNumber((privData.getAttributes().getNamedItem("mobile").getNodeValue()));
        contact.getPrivateData().setPhoneNumber((privData.getAttributes().getNamedItem("phone").getNodeValue()));
        contact.getPrivateData().setStreet((privData.getAttributes().getNamedItem("street").getNodeValue()));
        contact.getPrivateData().setPostalCode((privData.getAttributes().getNamedItem("postalcode").getNodeValue()));
        contact.getPrivateData().setCity((privData.getAttributes().getNamedItem("city").getNodeValue()));
        contact.getPrivateData().setCountry((privData.getAttributes().getNamedItem("country").getNodeValue()));
        contact.getPrivateData().setWebsite((privData.getAttributes().getNamedItem("website").getNodeValue()));
        contact.getPrivateData().setEmail((privData.getAttributes().getNamedItem("email").getNodeValue()));
        contact.getPrivateData().setRemark((privData.getAttributes().getNamedItem("remark").getNodeValue()));
        Node offData = data.item(1);
        contact.getOfficeData().setCompanyName((offData.getAttributes().getNamedItem("company").getNodeValue()));
        contact.getOfficeData().setDepartment((offData.getAttributes().getNamedItem("department").getNodeValue()));
        contact.getOfficeData().setOccupation((offData.getAttributes().getNamedItem("occupation").getNodeValue()));
        contact.getOfficeData().setMobilePhoneNumber((offData.getAttributes().getNamedItem("mobile").getNodeValue()));
        contact.getOfficeData().setPhoneNumber((offData.getAttributes().getNamedItem("phone").getNodeValue()));
        contact.getOfficeData().setStreet((offData.getAttributes().getNamedItem("street").getNodeValue()));
        contact.getOfficeData().setPostalCode((offData.getAttributes().getNamedItem("postalcode").getNodeValue()));
        contact.getOfficeData().setCity((offData.getAttributes().getNamedItem("city").getNodeValue()));
        contact.getOfficeData().setCountry((offData.getAttributes().getNamedItem("country").getNodeValue()));
        contact.getOfficeData().setWebsite((offData.getAttributes().getNamedItem("website").getNodeValue()));
        contact.getOfficeData().setEmail((offData.getAttributes().getNamedItem("email").getNodeValue()));
        return contact;
    }
}
