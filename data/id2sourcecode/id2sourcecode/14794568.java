    public void write(String country, Triple triple, boolean isDescriptionOfCountry) throws Exception {
        String continent = countryToContinent.getProperty(country);
        if (isDescriptionOfCountry) {
            if (allCountries == null) {
                Namespaces namespaces = new Namespaces();
                namespaces.addNamespace(NS_EUROPEANA_SCHEMA, "europeana");
                namespaces.addNamespace("http://www.w3.org/2003/01/geo/wgs84_pos#", "wgs");
                allCountries = SesameWriter.createRDFXMLWriter(new File(root, continent + "-all-countries.rdf"), namespaces, continent + "_" + country, "Selected fields pulled from Geonames about " + country + " done on " + new Date(), 1024, 1024, "This dataset was selected and converted from Geonames http://download.geonames.org/export/dump/readme.txt", "that is licensed under a Creative Commons Attribution 3.0 License,", "see http://creativecommons.org/licenses/by/3.0/", "The Data is provided \"as is\" without warranty or any representation of accuracy, timeliness or completeness.");
                allCountries.startRDF();
            }
            allCountries.handleTriple(triple);
        }
        SesameWriter writer = files.get(country);
        if (writer == null) {
            Namespaces namespaces = new Namespaces();
            namespaces.addNamespace(NS_EUROPEANA_SCHEMA, "europeana");
            namespaces.addNamespace("http://www.w3.org/2003/01/geo/wgs84_pos#", "wgs");
            writer = SesameWriter.createRDFXMLWriter(new File(root, continent + "/" + country + ".rdf"), namespaces, continent + "_" + country, "Selected fields pulled from Geonames about " + country + " done on " + new Date(), 1024, 1024, "This dataset was selected and converted from Geonames http://download.geonames.org/export/dump/readme.txt", "that is licensed under a Creative Commons Attribution 3.0 License,", "see http://creativecommons.org/licenses/by/3.0/", "The Data is provided \"as is\" without warranty or any representation of accuracy, timeliness or completeness.", "Technical note: isPartOf may refer to concepts that are not described in these files.");
            files.put(country, writer);
            writer.startRDF();
        }
        writer.handleTriple(triple);
    }
