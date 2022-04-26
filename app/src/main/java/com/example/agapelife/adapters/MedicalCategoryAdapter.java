package com.example.agapelife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.user_ui.user_home.UserHomeFragment;
import com.google.android.material.chip.Chip;

import java.util.List;

public class MedicalCategoryAdapter extends RecyclerView.Adapter<MedicalCategoryAdapter.ViewHolder> {
    List<MedicalCategoryResponse> categories;
    Context context;
    UserHomeFragment userHomeFragment;

    public MedicalCategoryAdapter(List<MedicalCategoryResponse> categories, Context context, UserHomeFragment userHomeFragment) {
        this.categories = categories;
        this.context = context;
        this.userHomeFragment = userHomeFragment;
    }

    @NonNull
    @Override
    public MedicalCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalCategoryAdapter.ViewHolder holder, int position) {
        MedicalCategoryResponse category = categories.get(position);

        holder.categoryName = category.getName();
        holder.idCategory = category.getId();
        holder.categoryChip.setText(holder.categoryName);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Chip categoryChip;
        String categoryName;
        int idCategory = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryChip = itemView.findViewById(R.id.category_chip);

            categoryChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(idCategory != 0){
                        userHomeFragment.filterDoctorCategory(idCategory);
                    }
                }
            });
        }
    }
}
