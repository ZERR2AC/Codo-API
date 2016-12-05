package Codo.Model.Response;

import Codo.Model.Channel;

/**
 * Created by terrychan on 24/11/2016.
 */
public class CreateChannelSucceedJsonResponse extends JsonResponse {
    private Channel channel;

    public CreateChannelSucceedJsonResponse(Channel channel) {
        super(0, "ok");
        this.channel = channel;
}
}
