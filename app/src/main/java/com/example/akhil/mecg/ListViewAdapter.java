package com.example.akhil.mecg;

/**
 * Created by Akhil on 23-03-2016.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<MedicalFiles> medicalFilesList = null;
    private ArrayList<MedicalFiles> arraylist;

    public ListViewAdapter(Context context,
                           List<MedicalFiles> worldpopulationlist) {
        this.context = context;
        this.medicalFilesList = worldpopulationlist;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<MedicalFiles>();
        this.arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView name;
        TextView centre;
    }

    @Override
    public int getCount() {
        return medicalFilesList.size();
    }

    @Override
    public Object getItem(int position) {
        return medicalFilesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            holder.name = (TextView) view.findViewById(R.id.txtName);
            holder.centre= (TextView) view.findViewById(R.id.txtCentre);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(medicalFilesList.get(position).getName());
        holder.centre.setText(medicalFilesList.get(position).getCentre());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, SingleFileShow.class);
                intent.putExtra("name",(medicalFilesList.get(position).getName()));
                intent.putExtra("centre",(medicalFilesList.get(position).getCentre()));
                intent.putExtra("fileid", (medicalFilesList.get(position).getObj().getObjectId()));

                context.startActivity(intent);
            }
        });
        return view;
    }

}