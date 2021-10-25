package logicreat.waitins;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaron on 2016-06-20.
 */
public class GeneralImageRequest extends Request<Bitmap>{

    private Priority mPriority = Priority.LOW;
    private Map<String, String> params;
    private Response.Listener<Bitmap> listener;

    private MyApp myInstance = MyApp.getOurInstance();

    public GeneralImageRequest(String URL, Map<String, String> params, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, errorListener);

        myInstance = MyApp.getOurInstance();

        this.listener = listener;
        this.params = params;
    }

    @Override
    public Map<String, String> getParams() {
        return this.params;
    }

    @Override
    public Response<Bitmap> parseNetworkResponse(NetworkResponse response){
        myInstance.checkSessionCookie(response.headers);
        String stringResponse = new String(response.data);
        Bitmap bmp;
        if (stringResponse.equals("$")){
            bmp = myInstance.getDefaultPicture();
        }
        else if (stringResponse.equals("@")) {
            bmp = null;
        }
        else if (stringResponse.equals("&")){
            bmp = myInstance.getUpToDateBitmap();
        }
        else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeByteArray(response.data, 0, response.data.length, options);
            options.inJustDecodeBounds=false;
            options.inSampleSize = myInstance.calculateInSampleSize(options, (MyApp.width/2), (MyApp.height/2));
            bmp = BitmapFactory.decodeByteArray(response.data, 0, response.data.length, options);
        }

        return Response.success(bmp, HttpHeaderParser.parseCacheHeaders((response)));
    }

    @Override
    public void deliverResponse(Bitmap response){
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        myInstance.addSessionCookie(headers);

        return headers;
    }

    @Override
    public Priority getPriority(){
        return mPriority;
    }
}
