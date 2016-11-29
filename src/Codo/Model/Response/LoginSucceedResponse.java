package Codo.Model.Response;

import Codo.Model.User;
import Codo.Util.CONSTANT;

public class LoginSucceedResponse extends Response {
    private String token;
    private User user;

    public LoginSucceedResponse(String token, User user) {
        super(CONSTANT.STATE.OK, "ok");
        this.token = token;
        this.user = user;
    }
}
