package android.com.freezeframe;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PreviewAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResId;
    ArrayList<String> data = null;

    public PreviewAdapter(Context context, ArrayList<String> data) {
        super(context, R.layout.frame_preview, data);
        this.layoutResId = layoutResId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder holder = null;

        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.frame_preview, parent, false);

            holder = new HistoryHolder();
            holder.imageIcon = (ImageView)convertView.findViewById(R.id.imageone);

            convertView.setTag(holder);
        }
        else
        {
            holder = (HistoryHolder)convertView.getTag();
        }

        String history = data.get(position);
        Picasso.with(this.context).load(history).into(holder.imageIcon);


        return convertView;
    }

    static class HistoryHolder
    {
        ImageView imageIcon;
    }
}