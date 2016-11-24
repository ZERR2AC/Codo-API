package Codo.Model.Response;

import Codo.Model.Response.Response;

public class LoginSucceedResponse extends Response {
    private String token;
    private String user_id;

    public LoginSucceedResponse(String token, String user_id) {
        super(0, "ok");
        this.token = token;
        this.user_id = user_id;
    }
}
