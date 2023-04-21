package no.uio.edd.model.geo;

import java.io.IOException;
import javax.swing.JSplitPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import no.uio.edd.utils.datautils.MessageViewer;
import no.uio.edd.utils.datautils.TableCallbackInterface;
import no.uio.edd.utils.xmlutils.DomUtils;

public class GeoModelProperties extends JSplitPane implements TableCallbackInterface {

    private static final long serialVersionUID = 1L;

    private GeoModelRunner myGeoModelRunner;

    private GeoModelPropertiesTableView myGeoModelPropertiesTableView;

    private GeoModelPropertiesDashboard myGeoModelPropertiesDashboard;

    public GeoModelProperties(GeoModelRunner inGeoModelRunner) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        myGeoModelRunner = inGeoModelRunner;
        myGeoModelPropertiesTableView = new GeoModelPropertiesTableView(this);
        myGeoModelPropertiesDashboard = new GeoModelPropertiesDashboard(this);
        this.setLeftComponent(myGeoModelPropertiesDashboard);
        this.setRightComponent(myGeoModelPropertiesTableView);
        this.setDividerLocation(GeoModel.PROPERTIES_MAIN_DIVIDER);
        loadPlaceRegTable();
    }

    void saveTypeChanges() {
        String selectedId = myGeoModelPropertiesTableView.getIdSelectedRow();
        String selectedType = myGeoModelPropertiesTableView.getTypeInSelectedRow();
        String selectedParsedType = myGeoModelPropertiesTableView.getParsedTypeInSelectedRow();
        GeoModelLink linkForUpdate = myGeoModelRunner.getMyGeoModelLinkSet().getLink(selectedId);
        linkForUpdate.setType(selectedType);
        if (selectedParsedType != null && !selectedParsedType.equals("")) try {
            linkForUpdate.setParsedType(selectedParsedType);
        } catch (GeoModelLinkException e) {
            MessageViewer.infoMessage(myGeoModelRunner.mainFrame, "Cannot save parsed link data, it does not parse.\n" + e.getMessage());
        }
        refreshTable();
    }

    void saveParsedTypeSelectedLines() {
        boolean parseError = false;
        String newParsedType = MessageViewer.getInput(myGeoModelRunner.mainFrame, "New value for the parsed type for these lines:");
        if (newParsedType != null && !newParsedType.equals("")) {
            String[] idsForText = getIdsForSelectedLinks();
            for (int i = 0; i < idsForText.length; i++) {
                if (!parseError) {
                    GeoModelLink linkForUpdate = myGeoModelRunner.getMyGeoModelLinkSet().getLink(idsForText[i]);
                    try {
                        linkForUpdate.setParsedType(newParsedType);
                    } catch (GeoModelLinkException e) {
                        MessageViewer.infoMessage(myGeoModelRunner.mainFrame, "Cannot save parsed link data, it does not parse.\n" + e.getMessage());
                        parseError = true;
                    }
                }
            }
            if (!parseError) refreshTable();
        }
    }

    private String[] getIdsForSelectedLinks() {
        int[] selectedRows = myGeoModelPropertiesTableView.getSelectedRows();
        String[] idsForSelected = new String[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            idsForSelected[i] = myGeoModelPropertiesTableView.getNodeIdInRow(selectedRows[i]);
            if (GeoModel.DEBUG > 1) System.out.println("Selected row: " + selectedRows[i] + " with ID " + idsForSelected[i]);
        }
        return idsForSelected;
    }

    void deleteSelectedLine() {
        String selectedId = myGeoModelPropertiesTableView.getIdSelectedRow();
        int yesOrNo = MessageViewer.question(myGeoModelRunner.mainFrame, "Delete property line with ID " + selectedId + "?");
        if (yesOrNo == 0) {
            GeoModelLink linkToBeRemoved = myGeoModelRunner.getMyGeoModelLinkSet().getLink(selectedId);
            if (linkToBeRemoved == null) MessageViewer.infoMessage(myGeoModelRunner.mainFrame, "Not able to remove this row: Does not exist."); else {
                boolean success = linkToBeRemoved.removeObject();
                if (!success) MessageViewer.infoMessage(myGeoModelRunner.mainFrame, "Not able to remove this row.");
            }
        }
        refreshTable();
    }

    void refreshTable() {
        myGeoModelPropertiesTableView.clearTable();
        loadPlaceRegTable();
    }

    private void loadPlaceRegTable() {
        loadTable(myGeoModelRunner.getMyGeoModelLinkSet().getLinkSet());
    }

    @Override
    public void cellLeft(int column, int row) {
    }

    @Override
    public void rowSelected(String rowValue, String tableType) {
    }

    @Override
    public void tableEscape() {
    }

    void loadTable(String inRdf) {
        inRdf = "<?xml version='1.0' encoding='utf-8'?><dummy>" + inRdf + "</dummy>";
        Document docRdf = DomUtils.parseStringAsXML(inRdf);
        NodeList propNodes, childNodes;
        propNodes = docRdf.getElementsByTagName("property");
        Element thisPropElem, thisChildElem;
        GeoModelPerson speakerPersonFrom, speakerPersonTo;
        String nodeName, linkId, from, fromString, fromPara, to, toString, toPara, type, typeParsed, speakerFrom, speakerTo;
        GeoModelLinkable linkable;
        for (int i = 0; i < propNodes.getLength(); i++) {
            linkId = "";
            from = "";
            fromString = "";
            fromPara = "";
            speakerFrom = "";
            to = "";
            toString = "";
            toPara = "";
            speakerTo = "";
            type = "";
            typeParsed = "";
            thisPropElem = (Element) propNodes.item(i);
            childNodes = thisPropElem.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) if (childNodes.item(j).getNodeType() == 1) {
                thisChildElem = (Element) childNodes.item(j);
                nodeName = thisChildElem.getNodeName();
                if (nodeName.equals("linkId")) linkId = thisChildElem.getTextContent(); else if (nodeName.equals("linkType")) type = thisChildElem.getTextContent(); else if (nodeName.equals("from")) from = thisChildElem.getTextContent(); else if (nodeName.equals("to")) to = thisChildElem.getTextContent(); else if (nodeName.equals("linkTypeParsed")) {
                    try {
                        typeParsed = DomUtils.xmlNodeContents(thisChildElem);
                    } catch (IOException e) {
                        if (GeoModel.DEBUG > 0) System.err.println("GeoModelProperties: loadTable: Could not parse the parsed type for link: " + linkId);
                        typeParsed = "";
                    }
                }
            }
            if (from.length() > 3) {
                linkable = myGeoModelRunner.getMyGeoModelLinkSet().getLink(linkId).getFrom();
                fromString = linkable.toString();
                fromPara = linkable.getIdOfPara();
                speakerPersonFrom = myGeoModelRunner.getMyGeoModelPersonReg().getGeoModelPersonFromSpeakerNodeId(fromPara);
                if (speakerPersonFrom != null) speakerFrom = speakerPersonFrom.getId().substring(6);
            }
            if (to.length() > 3) {
                linkable = myGeoModelRunner.getMyGeoModelLinkSet().getLink(linkId).getTo();
                toString = linkable.toString();
                toPara = linkable.getIdOfPara();
                speakerPersonTo = myGeoModelRunner.getMyGeoModelPersonReg().getGeoModelPersonFromSpeakerNodeId(toPara);
                if (speakerPersonTo != null) speakerTo = speakerPersonTo.getId().substring(6);
            }
            myGeoModelPropertiesTableView.putLine(linkId, from, fromString, fromPara, speakerFrom, to, toString, toPara, speakerTo, type, typeParsed);
        }
    }

    void popupText(boolean tagged) {
        String idForText = myGeoModelPropertiesTableView.getParaIdInSelectedRow();
        MessageViewer.longMessage(myGeoModelRunner.mainFrame, myGeoModelRunner.getTextFromSurroundingPara(idForText, tagged), "Paragraph level element ID: " + myGeoModelRunner.getIdOfSurroundingPara(idForText));
    }

    void linkReport() {
        String idForText = myGeoModelPropertiesTableView.getIdSelectedRow();
        String htmlReport = myGeoModelRunner.getMyGeoModelLinkSet().getLink(idForText).getInfoHtml();
        MessageViewer.longMessage(myGeoModelRunner.mainFrame, htmlReport, "Link ID: " + idForText, "text/html", 500, 200, 300, 500);
    }
}
