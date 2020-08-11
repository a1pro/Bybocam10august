package com.example.bybocam.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.bybocam.Interface.ListInterface;
import com.example.bybocam.Model.FriendsData;
import com.example.bybocam.R;
import java.util.List;

public class ListviewAdapter extends ArrayAdapter {
    List<FriendsData> data;
    Context context;
    private TextView txtName;
    private CheckBox checkBox;
    ListInterface listInterface;

    public ListviewAdapter(Context context, List<FriendsData> data, ListInterface listInterface) {
        super(context, R.layout.row_item, data);
        this.data=data;
        this.context=context;
        this.listInterface=listInterface;

    }



    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        txtName = convertView.findViewById(R.id.txtName);
        checkBox = convertView.findViewById(R.id.checkBox);

        txtName.setText(data.get(position).getUserName());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    listInterface.getId(data.get(position).getUserId(),position);
                }else {
                    listInterface.getId(null,position);
                }
            }
        });


        return convertView;
    }
}
