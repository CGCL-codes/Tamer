    public InstructionSet GetInstructionSetForStep(WalkthroughOptions options) {
        InstructionSet result = null;
        switch(GetSegment()) {
            case 0:
                {
                    switch(GetStep()) {
                        case 0:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A: Enter the map name in the space provided. This name will be used when displaying the map in the map selection window in TripleA. When finished, click continue...", null);
                                map.put("B: Enter the version of the map you are creating in the space provided. This number will be used when updating maps in TripleA, so make sure this is increased each time you release your map. When finished, click continue...", null);
                                map.put("C: If your map contains the entire world, you can enable scroll wrapping on your map. This will cause the map to be scrolled to the left edge when the player scrolls all the way to the right edge and continues. This results in a globe-like continuous scroll, kind of like a globe that is continuously spinning. If you want it enabled, select the scrolling type you want in the selection box provided. When finished, click continue...", null);
                                map.put("D: The size of the units when they are drawn in TripleA can be set using the slider provided. A preview of how large the units will be is shown to the right of the slider. Move the slider back and forth until you find the size that you want. When finished, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 1:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                switch(options.p_controlLevelOption) {
                                    case 0:
                                        {
                                            map.put("A: The purpose of this step is to create the map image, and to do this, you just draw the borders between each territory on the map, and since you've just begun, you can make it however you like. You may choose to create the map image in an external image editor and import it using the Load button.(Because the built-in image editor is currently unstable, we highly recommend that you do this) If you do choose to use the built-in image editor, the following information should get you started. Press continue to read more...", null);
                                            map.put("B: The pen and line tools are used to create the borders of the territories. You can set the size of the pen, line, and eraser tools by dragging the slider at the bottom of the toolbar left and right. After the territory borders are drawn, you should color the sea zones with the \"Sea\" tool to help differentiate the territories later. If you want to remove the blue color from a territory, you can use the \"Land\" tool. If you make a mistake, you can use the erase tool or the undo feature. Press continue to read more...", null);
                                            map.put("C: Make sure that you draw complete borders for the territories. If you even leave a tiny hole in the territory border, it will cause the territory border to become useless. The image you are drawing can also be resized using the small button at the bottom-right corner of the toolbar. When you finish the map image, click to continue to the next step...", null);
                                            break;
                                        }
                                    case 1:
                                        {
                                            map.put("A: The purpose of this step is to create the map image, and to do this, you just draw the borders between each territory on the map, and since you've just begun, you can make it however you like. You can either create the map image using the built-in image editor or create it in an external image editor and import it using the Load button.(Because the built-in image editor is currently unstable, we highly recommend that you do this) If you do choose to use the built-in image editor, the following information should get you started. Press continue to read more...", null);
                                            map.put("B: The pen and line tools are used to create the borders of the territories. You can set the size of the pen, line, and eraser tools by dragging the slider at the bottom of the toolbar left and right. After the territory borders are drawn, you should color the sea zones blue to help differentiate the territories later. If you make a mistake, you can use the erase tool or the undo feature. Press continue to read more...", null);
                                            map.put("C: The color of the current tool can be set by clicking the \"Color\" button and selecting a color, or by left-clicking on the image while holding control to select a color on the image. Make sure that you draw complete borders for the territories. If you even leave a tiny hole in the territory border, it will cause the territory border to become useless. The image you are drawing can also be resized using the small button at the bottom-right corner of the toolbar. When you finish the map image, click to continue to the next step...", null);
                                            break;
                                        }
                                    case 2:
                                        {
                                            map.put("A: The purpose of this step is to create the map image, and to do this, you just draw the borders between each territory on the map, and since you've just begun, you can make it however you like. You may choose to create the map image in an external image editor and import it using the Load button.(Because the built-in image editor is currently unstable, we highly recommend that you do this) If you do choose to use the built-in image editor, the following information should get you started. Press continue to read more...", null);
                                            map.put("B: The pen and line tools are used to create the borders of the territories. You can set the size of the pen, line, and eraser tools by changing the value of the number spinner at the bottom of the toolbar. After the territory borders are drawn, you should fill the sea zones with the color blue to help differentiate the territories later. If you make a mistake, you can use the erase tool or the undo feature. Press continue to read more...", null);
                                            map.put("C: The color of the current tool can be set by clicking the \"Color\" button and selecting a color, or by left-clicking on the image while holding control to select a color on the image. Make sure that you draw complete borders for the territories. If you even leave a tiny hole in the territory border, it will cause the territory border to become useless. The image you are drawing can also be resized using the small button at the bottom-right corner of the toolbar. When you finish the map image, click to continue to the next step...", null);
                                            break;
                                        }
                                }
                                return new InstructionSet(map);
                            }
                        case 2:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A: The purpose of this step is to define the locations and names of all the map's territories. To complete this step, simply left-click on the center of each territory (including sea zones), and enter the territory name in the box that appears. If you want to remove a territory, simply right-click on the small dot next to the name. If you want to remove all of the territory definitions at once, you can press the \"Clear\" button. When you finish adding all of the territories, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 3:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A: The purpose of this step is to define the polygons for all the map's territories. These polygons are the outlines of the territories that TripleA uses to color the territories while playing. To complete this step, simply double-right-click on the center of each territory (including sea zones), and enter the territory the polygon covers in the box that appears. It is recommended that you grab the island polygons first, because this will help increase the accuracy of the territory auto-guess feature. If you have island chains in your map, you can select the entire island chain by control-right-clicking on the individual parts until you select them all. Then just left-click on the main island and enter the territory that the chain completes. Press continue to read more...", null);
                                map.put("B: You also have the option of using the auto completion method which finds the polygons for all of the territories using the territory centers as the click points. You can start this scan by selecting the \"Auto\" button at the bottom-left of the toolbar. If you want to remove a polygon, simply right-click the polygon while holding shift. If you want to remove all of the map polygons, you can press the \"Clear\" button. There is also the island displaying mode on the toolbar which allows you to see islands when the sea zone polygons would otherwise cover them. The advanced polygon grabbing option on the toolbar allows the program to use extensive scanning when finding the polygons. This sometimes lets you find a polygon that would otherwise be impossible to scan for. When you finish adding all of the territory polygons, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 4:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A: The purpose of this step is to specify the connections between the map territories. If a territory borders another territory, you have a connection to add. To complete this step, simply left-click on the first territory in each connection, then click the one it borders. A line will appear on-screen linking the two territories. This shows that a connection is present. You can also remove any of the territory connections at any time by right-clicking on the line that links the two territories. Press continue to read more...", null);
                                map.put("B: There is also the auto-find scan that you can use that will try to automatically find all of the territory connections. Before you run the scan, though, remember to tell the program the width of the territory border lines. If you already added some of the connections yourself and you want to keep them after the scan completes, you can select the \"Have Auto-Find Only Add\" checkbox in the toolbar. When you finish adding all of the territory connections, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 5:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A: The purpose of this step is to specify where the program should draw the images for the units that will be contained in the territories. To complete this step, simply control-left-click on each territory you want to add drawing locations to, then left-click everywhere in the territory where you want units to be drawn. A box will appear on-screen showing the area where the unit will be drawn. You can also remove any of the unit drawing areas at any time by right-clicking in the box that represents the the unit drawing area. Press continue to read more...", null);
                                map.put("B: There is also the auto-find scan that you can use that will try to automatically find all of the good locations to draw units. If you want the program to overlap the unit placement areas a bit to fit more units in each territory, you can increase the value of the \"Placement Overlap\" spinner in the toolbar. When you finish adding all of the unit drawing locations, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                    }
                }
            case 1:
                {
                    switch(GetStep()) {
                        case 0:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. To complete this step, simply choose a preset that your map will be based off of. If you want to start your map xml from scratch, choose the 'Empty Preset' option. (Please note that this preset feature is far from finished with only the first three steps coded in. In the future, the entire map xml segment will have preset values for these five maps.) When you finish choosing your map's preset, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 1:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. The purpose of this step is to specify the names and data for all of the players(countries) in the map. To add a player, just press the 'New' button below the player list and enter the player name. Once a player is added to the list, you can click the player's name to edit the player's data in the box to the right of the list. Press continue to read more...", null);
                                map.put("B. The player data consists of the player name, flag(32x32 size required), color, start resources, and alliances. Each of these things can be modified by using the controls in the 'Player Data' box. Before you can specify the alliances each player belongs to, you must create the alliances by pressing the 'New' button under the alliance list and supplying each alliance name. Press continue to read more...", null);
                                map.put("C. Once you're done adding all the alliances, you must specify which ones each player belongs to. This can be done for each player by highlighting the alliances it belongs to by control-clicking the alliance names. When you finish entering all the data for each player in the map, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 2:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. The purpose of this step is to specify the order of the players' turns. To change the player order, simply select a player and use the Up and Down arrow buttons to change its turn position. When you finish setting the player order, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 3:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. The purpose of this step is to specify the names, images, costs, and properties for each unit. To add a unit, just press the 'New' button below the unit list and enter the unit name. Once a unit is added to the list, you can click the unit's name to edit the unit's data in the box to the right of the list. Press continue to read more...", null);
                                map.put("B. The unit data consists of the unit name and the player-specific unit images(48x48 size required) and purchase costs. Each of these things can be modified by using the controls in the 'Unit Data' box. Press continue to read more...", null);
                                map.put("C. To the right of the unit data box there is the unit properties box. This is where you specify what type of unit it is, how well it attacks, how well it defends, how far it can move, etc. When you finish entering all the data and properties for each unit, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 4:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                        case 5:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. The purpose of this step is to specify the basic territory information, such as the initial owners, the production values, etc. To do this, simply left-click on each territory and enter its data in the toolbar area. When you finish doing this for each territory, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 6:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. The purpose of this step is to place down all the units that will be on the map when a new game starts. To do this, simply left-click on each territory and choose it's start units using the toolbar at the bottom of the screen. When you finish doing this for each territory, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 7:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                        case 8:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. In this step, you get to change some map options that don't really fit into the other steps, like how much Bid money each player starts with and whether the bid amounts are changeable within TripleA. Bid money is different than normal because it's used by players before the game really starts, and the units purchased are placed down right away, anywhere on the map!(Though most maps/people have rules or guidelines limiting this somewhat) Press continue to read more...", null);
                                map.put("B. The Show Territory Names option determines whether TripleA should draw the name of each territory onto the map. The minimap scale option lets you choose the size of the navigational image that is shown at the top-right corner of the TripleA window.(The preview button is very helpful in getting this setting just right) When you finish setting the map options, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 9:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. This step lets you create and edit the map properties. The map properties are what let TripleA know how the game is supposed to be played, what rules are to be enforced, and stuff like that. New map properties are frequently added to TripleA, so though the list provided on the left is meant to be comprehensive, you may find a few(Actually... many) to be missing. Press continue to read more...", null);
                                map.put("B. Take a look back over at the property list. In it, you will notice the nine different categories. By expanding them, you'll be able to view and edit the values of any of the map properties. After clicking on a property, the panel to the right will display it's value, and you'll be able to change it there. Press continue to read more...", null);
                                map.put("C. Now that you know the basics, you can get started! If you get confused on what the different properties do, no problem, just post your questions on one of the forums and you should get a response within a few days. (That is, if we even know what it does) When you finish your map property editing, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                    }
                }
            case 2:
                {
                    switch(GetStep()) {
                        case 0:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                        case 1:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                        case 2:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                        case 3:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                    }
                }
            case 3:
                {
                    switch(GetStep()) {
                        case 0:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. In this step, you test your map out to make sure there are no problems with it. To do this, just press the button at the bottom, and when TripleA is launched, press Start Local Game. Then select the player types(you should test the map with AI's selected at least once) and press Start. If all goes well, you'll be able to play through an entire round without any problems. Press continue to read more...", null);
                                map.put("B. If you do encounter problems, whether they occur when TripleA is starting, you are trying to launch the game, or the game is in progress, and you're not able to figure out how to fix it, you can try asking for help on one of the forums:\r\n\r\nTripleA Development Forum: http://triplea.sourceforge.net/mywiki/Forum\r\nTripleA Map Creator Forum: http://sites.google.com/site/tripleamapcreator/forums\r\n\r\nPress continue to read more...", null);
                                map.put("C. Though we do have support forums open, we encourage users to try to fix these problems on their own, this way they will be able to learn from there mistakes and not have to depend on others and wait for them to get around to it. I really think that most error messages can be understood if they're read carefully and not just glanced over.(Don't be intimidated by the huge blocks of text, 75% of the time, the only part we look at is the first two or three lines) When you do finish fixing any and all map problems, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 1:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. In this step, you can create the map notes. Map notes can be accessed by players from within the game through the TripleA Help menu. With well-written map notes, players won't have to expiriment to determine the map rules or have to ask others to find out which players are allied, what units have what abilities, etc. Press continue to read more...", null);
                                map.put("B. You'll notice that on the right, there are a number of HTML Snippets listed. By double-clicking them, you'll be able to insert HTML code into your map notes. By using HTML in your map notes, you'll be able to embed images and tables, change the fonts, styles, or background colors of different parts of the document, create numbered or bulleted lists, and even write HTML comments that are hidden from players but visible to those editing your map. Press continue to read more...", null);
                                map.put("C. Though the snippet list given is far from complete, it includes some of the most commonly used HTML features, enough to really improve the quality and readability of your map notes. As a final tip, map notes usually include: a list of players and which alliances they belong to, a list of units with all their abilities and costs, the map rules, and maybe some strategic info or historical information. Have fun!(This part may take a while) When you finish the map notes, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                    }
                }
            case 4:
                {
                    switch(GetStep()) {
                        case 0:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Now that you've finished your map, it's time you released it. The first step to doing this is exporting the TripleA Map Project into a fully-working TripleA map. To do this, the program reads all the information you've entered up to this point and places the data into the appropriate files or folders so the map can be run in TripleA. So all you have to do in this step is press the button below! When you're done, click to continue to the next step...", null);
                                return new InstructionSet(map);
                            }
                        case 1:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. Coming soon...", null);
                                return new InstructionSet(map);
                            }
                        case 2:
                            {
                                TreeMap<String, BaseInstructionAction> map = new TreeMap<String, BaseInstructionAction>();
                                map.put("A. In this final step, you get to announce your map on the forums, and through doing this, contribute back to the TripleA community! What's great about this step is that you can do it however you want! All you got to do is press the button below, write up some intruiging description of your map and maybe include an image or two, and post a link to where your map is uploaded so people can try it! Have fun!", null);
                                return new InstructionSet(map);
                            }
                    }
                }
        }
        return result;
    }
