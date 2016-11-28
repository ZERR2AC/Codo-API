package Codo.Model.Response;

/**
 * Created by terrychan on 23/11/2016.
 */
public class Response {
    protected int ret;
    protected String msg;

    public Response(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }
}

