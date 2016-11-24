package Codo.Model.Response;

import Codo.Model.Channel;

/**
 * Created by terrychan on 24/11/2016.
 */
public class CreateChannelSucceedResponse extends Response {
    private Channel channel;

    public CreateChannelSucceedResponse(Channel channel) {
        super(0, "ok");
        this.channel = channel;
}
}
