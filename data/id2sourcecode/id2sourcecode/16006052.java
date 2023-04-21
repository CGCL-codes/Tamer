        public void writeBackParameter() throws KETLThreadException {
            Metadata md = ResourcePool.getMetadata();
            if (md == null) {
                throw new KETLThreadException("Parameter writeback failed as metadata could not be connected to", this);
            }
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.newDocument();
                Element paramList = document.createElement(EngineConstants.PARAMETER_LIST);
                Element parameter = document.createElement(EngineConstants.PARAMETER);
                document.appendChild(paramList);
                paramList.appendChild(parameter);
                parameter.setAttribute(ETLStep.NAME_ATTRIB, this.parameter);
                if (maxValue == null) newValue = null; else if (Number.class.isAssignableFrom(type)) {
                    newValue = Long.toString(((Number) maxValue).longValue());
                } else if (CharSequence.class.isAssignableFrom(type)) {
                    newValue = maxValue.toString();
                } else if (java.util.Date.class.isAssignableFrom(type)) {
                    newValue = Long.toString(((Timestamp) maxValue).getTime());
                } else {
                    throw new SQLException("incremental does not support " + type);
                }
                parameter.setTextContent(newValue);
                paramList.setAttribute(ETLStep.NAME_ATTRIB, this.parameterListName);
                md.importParameterList(paramList);
                ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.INFO_MESSAGE, "Updating incremental parameter " + this.getParameterName() + ", from " + this.getValue() + " to " + maxValue.toString());
            } catch (Exception e) {
                throw new KETLThreadException(e, this);
            }
        }
