    @Override
    public Set<Command> getCommands() {
        HashSet<Command> commands = new HashSet<Command>();
        commands.add(new CommandBase() {

            @Override
            public String[] getOptionalProperties() {
                return new String[] { "weather.location" };
            }

            @Override
            public String getCommandName() {
                return "weather";
            }

            @Override
            public void processCommand(String command, String parameter, OutputQueue queue) throws InvalidCommandException {
                String location = parameter;
                if (location == null || location.trim().equals("")) {
                    location = store.getString("weather.location");
                }
                if (location == null) {
                    throw new InvalidCommandException("A location must be specified");
                }
                Output output = new Output(this);
                try {
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser parser = factory.newSAXParser();
                    XMLReader reader = parser.getXMLReader();
                    String weatherString = WEATHER_XML_URL + location;
                    URL url = new URL(weatherString.replace(" ", "%20"));
                    GoogleWeatherHandler handler = new GoogleWeatherHandler();
                    URLConnection uc = url.openConnection();
                    HttpURLConnection connection = (HttpURLConnection) uc;
                    InputStream in = connection.getInputStream();
                    reader.setContentHandler(handler);
                    reader.parse(new InputSource(in));
                    if (!handler.isCompleted()) {
                        final Object wait = handler.getEndDocumentLock();
                        synchronized (wait) {
                            wait.wait();
                        }
                    }
                    WeatherSet set = handler.getWeatherSet();
                    output.addLine("Current Condition");
                    output.addLine("");
                    if (set.getWeatherCurrentCondition() != null) {
                        output.addLine("---- Today: ----");
                        output.addLine("");
                        output.addLine("Condition: " + set.getWeatherCurrentCondition().getCondition());
                        output.addLine("Humidity: " + set.getWeatherCurrentCondition().getHumidity());
                        output.addLine("Temp: " + set.getWeatherCurrentCondition().getTempCelcius());
                        output.addLine("Wind: " + set.getWeatherCurrentCondition().getWindCondition());
                    } else {
                        output.addLine("Current condition not found.");
                    }
                    output.addLine("");
                    output.addLine("--- Forecast --- ");
                    output.addLine("");
                    for (WeatherForecastCondition curr : set.getWeatherForecastConditions()) {
                        output.addLine("---- Day: " + curr.getDayofWeek() + " ----");
                        output.addLine("Condition: " + curr.getCondition());
                        output.addLine("Max: " + curr.getTempMaxCelsius());
                        output.addLine("Min: " + curr.getTempMinCelsius());
                    }
                } catch (ParserConfigurationException e) {
                    throw new InvalidCommandException(e);
                } catch (SAXException e) {
                    throw new InvalidCommandException(e);
                } catch (MalformedURLException e) {
                    throw new InvalidCommandException(e);
                } catch (IOException e) {
                    throw new InvalidCommandException(e);
                } catch (InterruptedException e) {
                    throw new InvalidCommandException(e);
                } finally {
                    queue.send(output);
                }
            }

            @Override
            public String getUsage() {
                return "<location>";
            }

            @Override
            public String getHelp() {
                return "Displays weather information";
            }
        });
        return commands;
    }
