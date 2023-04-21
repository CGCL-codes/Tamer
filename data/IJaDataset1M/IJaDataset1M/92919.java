package pierre.textui;

import pierre.model.*;
import pierre.api.*;
import pierre.system.BrowserServiceResources;
import pedro.mda.model.EditFieldModel;
import pedro.util.DisplayNameList;
import java.util.Date;
import java.util.ArrayList;
import java.net.URL;

public class ListMetaQueryFieldsAction extends BasicMenuDrivenAction {

    private ScreenControl mainMenuControl;

    private BasicMenuDrivenAction parentAction;

    private CannedQuery cannedQuery;

    private MetaQueryField metaQueryField;

    private DisplayNameList metaQueryFieldsList;

    private ScrollableScreenArea metaQueryFieldListArea;

    public ListMetaQueryFieldsAction(MenuApp menuApp, BasicMenuDrivenAction parentAction, CannedQuery cannedQuery) {
        super(menuApp);
        this.parentAction = parentAction;
        this.cannedQuery = cannedQuery;
        ArrayList metaQueryFields = cannedQuery.getMetaQueryFields();
        metaQueryFieldsList = new DisplayNameList(metaQueryFields);
        String mainMenuCode = BrowserServiceResources.getMessage("textui.general.controls.mainMenu.code");
        String mainMenuDisplayName = BrowserServiceResources.getMessage("textui.general.controls.mainMenu.displayName");
        mainMenuControl = new ScreenControl(mainMenuCode, mainMenuDisplayName);
        addControl(mainMenuControl);
        addQuitControl();
        resetScreen();
        addConstantArea(createTitleArea());
        metaQueryFieldListArea = new ScrollableScreenArea();
        updateChoiceListArea();
        setScrollableArea(metaQueryFieldListArea);
        establishPaddingArea();
        addControlArea();
    }

    private ConstantScreenArea createTitleArea() {
        ConstantScreenArea titleArea = new ConstantScreenArea();
        String titleText = BrowserServiceResources.getMessage("textui.listMetaQueryFieldEditingOptions.title", cannedQuery.getName());
        titleArea.addLine(titleText);
        titleArea.addLine(createDividerLine(titleText.length()));
        String instructionsText = BrowserServiceResources.getMessage("textui.listMetaQueryFieldEditingOptions.instructions");
        titleArea.addLine(instructionsText);
        titleArea.addLine("");
        return titleArea;
    }

    private void updateChoiceListArea() {
        metaQueryFieldListArea.clearLines();
        String[] displayNames = metaQueryFieldsList.getDisplayNames();
        for (int i = 0; i < displayNames.length; i++) {
            StringBuffer item = new StringBuffer();
            item.append(String.valueOf(i));
            item.append(". ");
            item.append(displayNames[i]);
            item.append("=");
            item.append("\"");
            MetaQueryField metaQueryField = (MetaQueryField) metaQueryFieldsList.getObject(i);
            item.append(metaQueryField.getValue());
            item.append("\"");
            metaQueryFieldListArea.addLine(item.toString());
        }
        metaQueryFieldListArea.initialiseNavigation(14);
    }

    public void run() {
        try {
            updateChoiceListArea();
            for (; ; ) {
                showScreen();
                String line = reader.readLine();
                ScreenControl result = determineSelectedScreenControl(line);
                if (result == mainMenuControl) {
                    menuApp.push(parentAction);
                    break;
                } else if (result == quitControl) {
                    quit();
                } else {
                    try {
                        Integer choice = Integer.valueOf(line);
                        int selectedItemIndex = choice.intValue();
                        boolean isIndexLegal = metaQueryFieldsList.isValidIndex(selectedItemIndex);
                        if (isIndexLegal == false) {
                            String error = BrowserServiceResources.getMessage("textui.general.controls.outOfIndex", line);
                            setError(error);
                        } else {
                            MetaQueryField metaQueryField = (MetaQueryField) metaQueryFieldsList.getObject(selectedItemIndex);
                            if (metaQueryField instanceof TextMetaQueryField) {
                                TextMetaQueryField textMetaQueryField = (TextMetaQueryField) metaQueryField;
                                UseFreeTextMetaQueryValueAction action = new UseFreeTextMetaQueryValueAction(menuApp, this, textMetaQueryField);
                                menuApp.push(action);
                                break;
                            } else {
                                CombinationMetaQueryField combinationMetaQueryField = (CombinationMetaQueryField) metaQueryField;
                                UseCombinationMetaQueryValueAction action = new UseCombinationMetaQueryValueAction(menuApp, this, combinationMetaQueryField);
                                menuApp.push(action);
                                break;
                            }
                        }
                    } catch (Exception err) {
                        String error = BrowserServiceResources.getMessage("textui.general.controls.unknownCommand", line);
                        setError(error);
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace(System.out);
        }
    }
}
