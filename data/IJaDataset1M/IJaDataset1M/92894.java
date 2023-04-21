package src.scenario.loader;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import src.enums.Bars;
import src.exceptions.BadScenarioFileException;

/**
 * This class will be passed the XML element for reading the bars
 * information in the scenario file.  It will read each bar type
 * in the scenario file and process the information.
 * @author Darren Watts
 * date 1/9/08
 *
 */
public class BarLoader {

    /**
	 * Constructor for the ArmorLoader.  Takes the defense node in the
	 * scenario file as a parameter.  Throws an exception if the information
	 * in the defense section of the scenario file isn't formatted
	 * correctly/complete.
	 * @param node Node : defense node
	 * @throws BadScenarioFileException
	 */
    public BarLoader(Node node) throws BadScenarioFileException {
        try {
            processBar(node);
        } catch (BadScenarioFileException bad) {
            throw new BadScenarioFileException();
        }
    }

    /**
	 * Reads each bar type and assigns the values into the correct
	 * bar enum.
	 * @param node Node : bar node
	 * @throws BadScenarioFileException
	 */
    public void processBar(Node node) throws BadScenarioFileException {
        try {
            ArrayList<Node> filteredNodeList = ScenarioLoader.getFilteredList(node);
            int currentType = 0;
            String tonicWater = "";
            String soda = "";
            String gin = "";
            String rum = "";
            String scotch = "";
            String redeye = "";
            for (int i = 0; i < filteredNodeList.size(); i++) {
                Element currentElement = (Element) filteredNodeList.get(i);
                currentType = Integer.parseInt(currentElement.getAttribute("type"));
                tonicWater = currentElement.getElementsByTagName("tonicwater").item(0).getFirstChild().getNodeValue();
                soda = currentElement.getElementsByTagName("soda").item(0).getFirstChild().getNodeValue();
                gin = currentElement.getElementsByTagName("gin").item(0).getFirstChild().getNodeValue();
                rum = currentElement.getElementsByTagName("rum").item(0).getFirstChild().getNodeValue();
                scotch = currentElement.getElementsByTagName("scotch").item(0).getFirstChild().getNodeValue();
                redeye = currentElement.getElementsByTagName("redeye").item(0).getFirstChild().getNodeValue();
                Bars.values()[currentType].setStats(tonicWater, soda, gin, rum, scotch, redeye);
            }
        } catch (Exception e) {
            System.err.println("Your bar information is invalid.");
            throw new BadScenarioFileException();
        }
    }
}
