    public void run() {
        try {
            Socket socket = new Socket(host.getHostName(), host.getPort());
            conn.bind(socket, params);
            BasicHttpRequest request;
            if (!this.request.getBody().isEmpty()) {
                request = new BasicHttpEntityEnclosingRequest(this.request.getMethod(), this.request.getPath());
            } else {
                request = new BasicHttpRequest(this.request.getMethod(), this.request.getPath());
            }
            Map<String, String> headers = this.request.getHeader();
            Iterator<Map.Entry<String, String>> it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pairs = it.next();
                request.addHeader(pairs.getKey(), pairs.getValue());
            }
            if (request instanceof BasicHttpEntityEnclosingRequest) {
                StringEntity body = new StringEntity(this.request.getBody());
                ((BasicHttpEntityEnclosingRequest) request).setEntity(body);
            }
            logger.info("> " + request.getRequestLine().getUri());
            request.setParams(params);
            httpexecutor.preProcess(request, httpproc, context);
            HttpResponse response = httpexecutor.execute(request, conn, context);
            response.setParams(params);
            httpexecutor.postProcess(response, httpproc, context);
            logger.info("< " + response.getStatusLine());
            this.response = new Response(response);
            for (int i = 0; i < this.responseFilter.size(); i++) {
                try {
                    this.responseFilter.get(i).exec(this.response);
                } catch (Exception e) {
                    Aletheia.handleException(e);
                }
            }
            callback.response(this.response.toString());
        } catch (Exception e) {
            Aletheia.handleException(e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                Aletheia.handleException(e);
            }
        }
    }
