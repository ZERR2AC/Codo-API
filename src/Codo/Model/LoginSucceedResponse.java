package Codo.Model;

public class LoginSucceedResponse extends Response {
    private String token;
    private String user_id;

    public LoginSucceedResponse(int ret, String msg, String token, String user_id) {
        super(ret, msg);
        this.token = token;
        this.user_id = user_id;
    }
}
