    public static void serialize(IAlgorithm alg, IGraph graph, IParameterInventory inventory, OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.putNextEntry(new ZipEntry("Graph"));
        OutputStreamWriter w = new OutputStreamWriter(zos);
        w.write("<GraphInfo>\n");
        w.write("\t<algorithm>");
        w.write(alg.getClass().getName());
        w.write("</algorithm>\n");
        w.write("\t<graph>\n");
        w.write("\t\t<name>");
        w.write(graph.getGraphName());
        w.write("</name>\n");
        w.write("\t\t<abscissa>");
        w.write(graph.getAbscissaName());
        w.write("</abscissa>\n");
        w.write("\t\t<ordinate>");
        w.write(graph.getOrdinateName());
        w.write("</ordinate>\n");
        w.write("\t\t<description>");
        w.write(graph.getDescription());
        w.write("</description>\n");
        w.write("\t\t<cfamily>\n");
        for (Entry<String, Object> ntr : graph.getCurveFamily().entrySet()) {
            w.write("\t\t\t<ntr><k>");
            w.write(ntr.getKey());
            w.write("</k><v><![CDATA[");
            w.write(alg.getParameter(ntr.getKey()).serialize(ntr.getValue()));
            w.write("]]></v></ntr>\n");
        }
        w.write("\t\t</cfamily>\n");
        w.write("\t</graph>\n");
        w.write("\t<inventory>\n");
        w.write("\t\t<runs>");
        w.write(String.valueOf(inventory.getRunsPerSet()));
        w.write("</runs>\n");
        w.write("\t\t<changing>");
        for (String s : inventory.getChangingParamNames()) {
            w.write("<n>");
            w.write(s);
            w.write("</n>");
        }
        w.write("</changing>\n");
        w.write("\t\t<addit_handlers>\n");
        for (AdditionalHandler ah : inventory.getAdditionalHandlers()) {
            w.write("\t\t\t<ah>");
            w.write("<handlername>");
            w.write(ah.getHandlerName());
            w.write("</handlername>");
            w.write("<actualvaluename>");
            w.write(ah.getActualValueName());
            w.write("</actualvaluename>");
            w.write("<handlertype>");
            w.write(ah.getHandler());
            w.write("</handlertype>");
            w.write("</ah>\n");
        }
        w.write("\t\t</addit_handlers>\n");
        w.write("\t\t<paramMaps>\n");
        for (Map<String, IValue> inputMap : inventory) {
            Map<String, IValue> retMap = inventory.getReturnValues(inputMap);
            w.write("\t\t\t<mapEntry>\n");
            w.write("\t\t\t\t<input>\n");
            for (Entry<String, IValue> ntr : inputMap.entrySet()) {
                w.write("\t\t\t\t\t<ntr><k>");
                w.write(ntr.getKey());
                w.write("</k><v><![CDATA[");
                w.write(ntr.getValue().parameter().serialize(ntr.getValue().value()));
                w.write("]]></v></ntr>\n");
            }
            w.write("\t\t\t\t</input>\n");
            w.write("\t\t\t\t<return>\n");
            for (Entry<String, IValue> ntr : retMap.entrySet()) {
                w.write("\t\t\t\t\t<ntr><k>");
                w.write(ntr.getKey());
                w.write("</k><v><![CDATA[");
                w.write(ntr.getValue().parameter().serialize(ntr.getValue().value()));
                w.write("]]></v></ntr>\n");
            }
            w.write("\t\t\t\t</return>\n");
            w.write("\t\t\t</mapEntry>\n");
        }
        w.write("\t\t</paramMaps>\n");
        w.write("\t</inventory>\n");
        w.write("</GraphInfo>");
        w.flush();
        zos.closeEntry();
        zos.close();
    }
