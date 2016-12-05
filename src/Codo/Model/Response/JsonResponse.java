package Codo.Model.Response;

import Codo.Util.Json;

import java.io.PrintWriter;

/**
 * Created by terrychan on 23/11/2016.
 */
public class JsonResponse {
    protected int ret;
    protected String msg;
    protected PrintWriter writer;

    public JsonResponse(int ret, String msg, PrintWriter writer) {
        this.ret = ret;
        this.msg = msg;
        this.writer = writer;
    }

    public void makeResponse() {
        writer.write(Json.getGson().toJson(this));
        writer.close();
    }
}

