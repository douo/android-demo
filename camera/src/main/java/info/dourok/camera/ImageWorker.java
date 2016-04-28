package info.dourok.camera;

import android.os.AsyncTask;

public abstract class ImageWorker extends AsyncTask<byte[], Integer, Boolean> {
    protected BaseCameraActivity context;

    public ImageWorker(BaseCameraActivity context){
        this.context = context;
    }

    public abstract boolean processing(byte[] data);

    @Override
    protected final void onPreExecute() {
    }

    @Override
    protected final void onPostExecute(Boolean bool) {
        context.onDoneProcessing(this);
        context = null;
    }

    public void destroy(){
        if(!isCancelled()) {
            cancel(true);
        }
        context = null;
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
        if(aBoolean){
            context = null;
        }
    }

    @Override
    protected final void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @SafeVarargs
    @Override
    protected final Boolean doInBackground(byte[]... params) {
        return processing(params[0]);
    }
}