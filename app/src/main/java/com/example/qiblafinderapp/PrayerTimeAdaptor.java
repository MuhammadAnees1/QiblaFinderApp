package com.example.qiblafinderapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qiblafinderapp.databinding.NamzTimeViewBinding;

import java.util.ArrayList;

class PrayerTimeAdapter extends RecyclerView.Adapter<PrayerTimeAdapter.ProductViewHolder> {
    private Context context;
    private ArrayList<NamazModel> classArrayList;

    public PrayerTimeAdapter(Context context, ArrayList<NamazModel> products) {
        this.context = context;
        this.classArrayList = products;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.namz_time_view, parent, false);
        return new ProductViewHolder(view);
    }
    @Override

    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        NamazModel item = classArrayList.get(position);

        holder.binding.textDate.setText("Fajr: " + item.getDate());
        holder.binding.textFajr.setText("Fajr: " + item.getFajr());
        holder.binding.textSunrise.setText("Sunrise: " + item.getSunrise());
        holder.binding.textDhuhr.setText("Dhuhr: " + item.getDhuhr());
        holder.binding.textAsr.setText("Asr: " + item.getAsr());
        holder.binding.textSunset.setText("Sunset: " + item.getSunset());
        holder.binding.textMaghrib.setText("Maghrib: " + item.getMaghrib());
        holder.binding.textIsha.setText("Isha: " + item.getIsha());
        holder.binding.textImsak.setText("Imsak: " + item.getImsak());
        holder.binding.textMidnight.setText("Midnight: " + item.getMidnight());
        holder.binding.textFirstthird.setText("First Third: " + item.getFirstThird());
        holder.binding.textLastthird.setText("Last Third: " + item.getLastThird());

    }

    @Override
    public int getItemCount() {
        return classArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        NamzTimeViewBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NamzTimeViewBinding.bind(itemView);
        }
    }
}
