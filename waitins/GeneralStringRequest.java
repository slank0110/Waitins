package logicreat.waitins;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 2016-06-20.
 */
public class GeneralStringRequest extends StringRequest {

    private Map<String, String> params;

    private MyApp myInstance;

    private Priority mPriority = Priority.HIGH;

    public GeneralStringRequest(String URL, Map<String, String> params, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        myInstance = MyApp.getOurInstance();
        this.params = params;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    @Override
    public Response<String> parseNetworkResponse(NetworkResponse response){
        myInstance.checkSessionCookie(response.headers);

        try {
            String utf8String = new String(response.data, "UTF-8");
            return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return super.parseNetworkResponse(response);
        }

        //return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

        myInstance.addSessionCookie(headers);

        return headers;
    }

    @Override
    public Priority getPriority(){
        return mPriority;
    }
}
