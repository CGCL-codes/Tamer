    public void xestNonAsciiLink() throws MalformedURLException, IOException {
        HashMap<String, String> testUrls = new HashMap<String, String>();
        testUrls.put("http://wayback.archive-it.org/1100/20080721212134/http://www.marca.com/futbol/madrid_vs_barca/previa/barca/barcaOK.swf", "barca/delape%C3%B1a.swf");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.contraloriagen.gov.co/html/publicaciones/imagenes/analisis-proyec-ley.swf", "http://www.contraloriagen.gov.co:8081/internet/html/publicaciones/por_dependencia_y_clase.jsp?clases=3&titulo_pagina=An%C3%A1lisis%20a%20Proyectos%20de%20Ley%20y%20Actos%20Legislativos");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.ine.gov.ve/secciones/modulos/Apure/sApure.swf", "aspectosfisicos.asp?Codigo=Nacimientos&titulo=Nacimientos%20vivos%20registrados%20por%20a%C3%B1o,%20seg%C3%BAn%20municipio%20de%20residencia%20habitual%20de%20la%20madre,%201999-2002&Fuente=Fuente:%20Prefecturas%20y%20Jefaturas%20Civiles&cod_ent=13&nvalor=2_2&seccion=2");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.ine.gov.ve/secciones/modulos/Lara/sLara.swf", "aspectosfisicos.asp?Codigo=Nacimientos&titulo=Nacimientos%20vivos%20registrados%20por%20a%C3%B1o,%20seg%C3%BAn%20municipio%20de%20residencia%20habitual%20de%20la%20madre,%201999-2002&Fuente=Fuente:%20Prefecturas%20y%20Jefaturas%20Civiles&cod_ent=13&nvalor=2_2&seccion=2");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.minsa.gob.pe/hnhipolitounanue/text13.swf", "archivos%20cuerpo/APOYO%20A%20LA%20DOCENCIA%20E%20INVESTIG/Registro%20de%20Estudios%20Cl%C3%ADnicos.pdf");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.nacobre.com.mx/flash/Flash_mercados.swf", "NSMcdoAccesoriosBa%C3%B1o.asp");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.sagarpa.gob.mx/dlg/nuevoleon/ddr's/Montemorelos/text4.swf", "campa%C3%B1a_abeja.htm");
        testUrls.put("http://wayback.archive-it.org/176/20080610233230/http://www.sagarpa.gob.mx/dlg/tabasco/text2.swf", "delegacion/comunicacion/cartel%20reuni%C3%B3n%20forestal%20xviii%20media2.pdf");
        testUrls.put("http://wayback.archive-it.org/317/20061129141640/http://www.ine.gov.ve/secciones/modulos/Miranda/sMiranda.swf", "aspectosfisicos.asp?Codigo=Nacimientos&titulo=Nacimientos%20vivos%20registrados%20por%20a%C3%B1o,%20seg%C3%BAn%20municipio%20de%20residencia%20habitual%20de%20la%20madre,%201999-2002&Fuente=Fuente:%20Prefecturas%20y%20Jefaturas%20Civiles&cod_ent=13&nvalor=2_2&seccion=2");
        testUrls.put("http://wayback.archive-it.org/317/20061129141640/http://www.ine.gov.ve/secciones/modulos/Tachira/sTachira.swf", "aspectosfisicos.asp?Codigo=Nacimientos&titulo=Nacimientos%20vivos%20registrados%20por%20a%C3%B1o,%20seg%C3%BAn%20municipio%20de%20residencia%20habitual%20de%20la%20madre,%201999-2002&Fuente=Fuente:%20Prefecturas%20y%20Jefaturas%20Civiles&cod_ent=13&nvalor=2_2&seccion=2");
        for (String url : testUrls.keySet()) {
            HttpRecorder recorder = HttpRecorder.wrapInputStreamWithHttpRecord(getTmpDir(), this.getClass().getName(), new URL(url).openStream(), null);
            CrawlURI curi = setupCrawlURI(recorder, url);
            long startTime = System.currentTimeMillis();
            this.extractor.innerProcess(curi);
            long elapsed = System.currentTimeMillis() - startTime;
            logger.info(this.extractor.getClass().getSimpleName() + " took " + elapsed + "ms to process " + url);
            boolean foundIt = false;
            for (Link link : curi.getOutLinks()) {
                logger.info("found link: " + link);
                foundIt = foundIt || link.getDestination().toString().endsWith(testUrls.get(url));
            }
            if (!foundIt) logger.severe("failed to extract link \"" + testUrls.get(url) + "\" from " + url);
            assertTrue("failed to extract link \"" + testUrls.get(url) + "\" from " + url, foundIt);
        }
    }
