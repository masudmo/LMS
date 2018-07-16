package com.example.mariyamasud.maps.register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mariyamasud.maps.R;
import com.example.mariyamasud.maps.event.Event;

import java.util.List;

/**
 * Created by mariyamasud on 20.02.18.
 */

public class RegisterList extends ArrayAdapter<Register> {
    private Activity context;
    /*to store all users

     */
    List<Register> registers;
    /*constructor

     */
    public RegisterList(Activity context, List<Register> registers) {
        super(context, R.layout.activity_list_register, registers);
        this.context = context;
        this.registers = registers;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_list_register, null, true);

        TextView nameTextView = (TextView) listViewItem.findViewById(R.id.name);
        TextView dateTextView = (TextView) listViewItem.findViewById(R.id.date);
        TextView languageTextView = (TextView) listViewItem.findViewById(R.id.language);
        TextView addressTextView = (TextView) listViewItem.findViewById(R.id.address);
        TextView phoneTextView = (TextView) listViewItem.findViewById(R.id.phone);
        TextView emailTextView = (TextView) listViewItem.findViewById(R.id.email);
        TextView grupeTextView = (TextView) listViewItem.findViewById(R.id.grupe);
        TextView occupationTextView = (TextView) listViewItem.findViewById(R.id.occupation);

        Register register = registers.get(position);
        nameTextView .setText(register.getName());
        dateTextView.setText(register.getDateOfBirth());
        languageTextView.setText(register.getLanguage());
        addressTextView.setText(register.getAddress());
        phoneTextView.setText(register.getPhone());
        emailTextView.setText(register.getEmail());
        grupeTextView.setText(register.getGroup());
        occupationTextView.setText(register.getOccupation());


        return listViewItem;
    }

}
