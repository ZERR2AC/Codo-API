package Codo.Model.Response;

import Codo.Util.CONSTANT;

public class LoginSucceedResponse extends Response {
    private String token;
    private int user_id;

    public LoginSucceedResponse(String token, int user_id) {
        super(CONSTANT.STATE.OK, "ok");
        this.token = token;
        this.user_id = user_id;
    }
}
