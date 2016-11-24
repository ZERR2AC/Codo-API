package Codo.Model.Response;

import Codo.Model.Channel;
import Codo.Util.CONSTANT;

import java.util.List;

/**
 * Created by terrychan on 25/11/2016.
 */
public class GetChannelsSucceedResponse extends Response {
    List<Channel> channels;

    public GetChannelsSucceedResponse(List<Channel> channels) {
        super(CONSTANT.STATE.OK, "ok.");
        this.channels = channels;
    }
}
