package com.example.App.weatherApp.Adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.App.weatherApp.Model.CityDetail;
import com.example.ngoctri.mapdirectionsample.R;

import java.util.ArrayList;
import java.util.List;

public class CitiesAdaptor extends RecyclerView.Adapter<CitiesAdaptor.MyViewHolder> implements Filterable {
    List<CityDetail> list;
    List<CityDetail> listCopy;

    Context context;

    public CitiesAdaptor(List<CityDetail> list, Context context, ContactsAdapterListener listener) {
        this.list = list;
        this.context = context;
        this.listCopy = new ArrayList<>();
        listCopy = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.city_item, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CityDetail data = list.get(position);
        holder.humidity.setText("Humidity: " + data.getHumidity());
        holder.weatherDes.setText(data.getWeatherDes());
        holder.windSp.setText("Wind Speed: " + data.getWindSp());
        holder.windDeg.setText("Wind Degree: " + data.getWindDeg());
        holder.maxTemp.setText(data.getMaxTemp());
        holder.minTemp.setText(data.getMinTemp());
        holder.currentTemp.setText(data.getCurrentTemp() + "C");
        holder.name.setText(data.getName());

        //holder.preasure.setText(data.getPreasure());

    }

    @Override
    public int getItemCount() {

        return listCopy.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listCopy = list;
                } else {
                    List<CityDetail> filteredList = new ArrayList<>();
                    for (CityDetail row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listCopy = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listCopy;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listCopy = (ArrayList<CityDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(CityDetail contact);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, currentTemp, minTemp, maxTemp, humidity, preasure, windDeg, windSp, weatherDes;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            currentTemp = view.findViewById(R.id.currTemp);
            minTemp = view.findViewById(R.id.minTemp);
            maxTemp = view.findViewById(R.id.maxTemp);
            windDeg = view.findViewById(R.id.windDg);
            windSp = view.findViewById(R.id.windSp);
            weatherDes = view.findViewById(R.id.weatheDes);
            humidity = view.findViewById(R.id.humidity);
            //  preasure = view.findViewById(R.id.pr);


        }
    }

    public void filter(CharSequence sequence) {
        ArrayList<CityDetail> temp = new ArrayList<>();
        if (!TextUtils.isEmpty(sequence)) {
            for (CityDetail s : list) {
                if (s.getName().toLowerCase().contains(sequence)) {
                    temp.add(s);
                }
            }
        } else {
            temp.addAll(listCopy);
        }
        list.clear();
        list.addAll(temp);
        notifyDataSetChanged();
        // temp.clear();
    }
}
