package info.dourok.android.demo.loader;

import android.content.Context;
import android.content.Loader;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


/**
 * Created by larry on 6/14/16.
 */
public abstract class VolleyLoader<D> extends Loader<D> {
    private RequestQueue mRequestQueue;
    private Request<D> mRequest;
    private int method;
    private String url;
    private VolleyError mError;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public VolleyLoader(Context context, RequestQueue requestQueue, int method, String url) {
        super(context);
        mRequestQueue = requestQueue;
        this.method = method;
        this.url = url;
    }

    /**
     * 数据源问题，Volley 的数据源应该在服务器，要做到数据源变更通知 Loader 未免太复杂了。
     * 如果是获取到的本地数据作为数据源，那么就会出现两个数据源，首次数据得去服务器拿，内容变更又拿本地的。
     *
     * @param context
     * @param method
     * @param url
     */
    public VolleyLoader(Context context, int method, String url) {
        this(context, Volley.newRequestQueue(context), method, url);
    }

    protected abstract Request<D> createRequest(int method, String url, Response.Listener<D> response, Response.ErrorListener listener);

    @Override
    protected void onStartLoading() {
        mRequest = createRequest(method, url, new Response.Listener<D>() {
            @Override
            public void onResponse(D response) {
                deliverResult(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                deliverResult(null);
                mError = error;
            }
        });
        mRequestQueue.add(mRequest);
    }

    @Override
    protected void onStopLoading() {
        mRequest.cancel();
    }

    @Override
    protected boolean onCancelLoad() {
        mRequest.cancel();
        return true;
    }

    @Override
    protected void onReset() {
        mRequest.cancel();
    }

    public VolleyError getError(){
        return mError;
    }

    protected abstract Response<D> parseResponse(NetworkResponse response);

    protected class LoaderRequest extends Request<D> {
        public LoaderRequest(int method, String url, Response.ErrorListener listener) {
            super(method, url, listener);
        }

        @Override
        protected Response<D> parseNetworkResponse(NetworkResponse response) {
            return parseResponse(response);
        }

        @Override
        protected void deliverResponse(D response) {
            deliverResult(response);
        }
    }


}
