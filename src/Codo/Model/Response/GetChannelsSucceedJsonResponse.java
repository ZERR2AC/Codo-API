package Codo.Model.Response;

import Codo.Model.Channel;
import Codo.Util.CONSTANT;

import java.util.List;

/**
 * Created by terrychan on 25/11/2016.
 */
public class GetChannelsSucceedJsonResponse extends JsonResponse {
    List<Channel> channels;

    public GetChannelsSucceedJsonResponse(List<Channel> channels) {
        super(CONSTANT.STATE.OK, "ok.");
        this.channels = channels;
    }
}
