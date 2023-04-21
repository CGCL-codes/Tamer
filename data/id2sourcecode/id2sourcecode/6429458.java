    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String query = getQuery(uri);
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        if (!isNetworkConnected()) {
            Log.i(LOG_TAG, "Not connected to network.");
            return null;
        }
        try {
            query = URLEncoder.encode(query, "UTF-8");
            if (mSuggestUri == null) {
                Locale l = Locale.getDefault();
                String language = l.getLanguage();
                String country = l.getCountry().toLowerCase();
                if ("zh".equals(language)) {
                    if ("cn".equals(country)) {
                        language = "zh-CN";
                    } else if ("tw".equals(country)) {
                        language = "zh-TW";
                    }
                } else if ("pt".equals(language)) {
                    if ("br".equals(country)) {
                        language = "pt-BR";
                    } else if ("pt".equals(country)) {
                        language = "pt-PT";
                    }
                }
                mSuggestUri = getContext().getResources().getString(R.string.google_suggest_base, language, country) + "json=true&q=";
            }
            String suggestUri = mSuggestUri + query;
            if (DBG) Log.d(LOG_TAG, "Sending request: " + suggestUri);
            HttpGet method = new HttpGet(suggestUri);
            HttpResponse response = mHttpClient.execute(method);
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONArray results = new JSONArray(EntityUtils.toString(response.getEntity()));
                JSONArray suggestions = results.getJSONArray(1);
                JSONArray popularity = results.getJSONArray(2);
                if (DBG) Log.d(LOG_TAG, "Got " + suggestions.length() + " results");
                return new SuggestionsCursor(suggestions, popularity);
            } else {
                if (DBG) Log.d(LOG_TAG, "Request failed " + response.getStatusLine());
            }
        } catch (UnsupportedEncodingException e) {
            Log.w(LOG_TAG, "Error", e);
        } catch (IOException e) {
            Log.w(LOG_TAG, "Error", e);
        } catch (JSONException e) {
            Log.w(LOG_TAG, "Error", e);
        }
        return null;
    }
