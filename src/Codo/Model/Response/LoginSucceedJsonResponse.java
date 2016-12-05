package Codo.Model.Response;

import Codo.Model.User;
import Codo.Util.CONSTANT;

public class LoginSucceedJsonResponse extends JsonResponse {
    private String token;
    private User user;

    public LoginSucceedJsonResponse(String token, User user) {
        super(CONSTANT.STATE.OK, "ok");
        this.token = token;
        this.user = user;
    }
}
