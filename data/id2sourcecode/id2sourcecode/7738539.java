        private <T extends Model> void write(Context context, final T model, final WriterCallback<T> callback, final RequestBuilder requestBuilder, final boolean readResponseFully) {
            RequestCallback translationCallback = new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    callback.onFailure(exception);
                }

                public void onResponseReceived(Request request, Response response) {
                    try {
                        List<T> models = new ArrayList<T>();
                        JSONArray array = JSONParser.parse(response.getText()).isArray();
                        if (readResponseFully) {
                            ModelReader<T> reader = reader(model.getClass());
                            T model = reader.readFully(array);
                            models.add(model);
                        } else {
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject o = array.get(i).isObject();
                                ModelReader<T> reader = reader(o);
                                models.add(reader.readPrototype(o));
                            }
                        }
                        callback.onSuccess(models);
                    } catch (ModelIOException e) {
                        callback.onFailure(e);
                    }
                }
            };
            try {
                ModelWriter<T> writer = writer(model.getClass());
                String s = writer.writePrototype(context, model).toString();
                requestBuilder.sendRequest(s, translationCallback);
            } catch (RequestException e) {
                callback.onFailure(e);
            } catch (ModelIOException e) {
                callback.onFailure(e);
            }
        }
