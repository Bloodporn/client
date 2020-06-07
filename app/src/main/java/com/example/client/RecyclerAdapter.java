package com.example.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> implements Filterable {

    private Context context;
    private ArrayList<FileData> allFiles;
    private  ArrayList<FileData> curFiles;

    RecyclerAdapter(ArrayList<FileData> files, Context context) {
        this.context = context;
        allFiles=new ArrayList<>(files);
        curFiles=files;
    }

    void swithcData(ArrayList<FileData> a){
        allFiles=new ArrayList<>(a);
        curFiles=new ArrayList<>(a);;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemViewHolder(LayoutInflater.from(context).inflate(R.layout.storage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder holder, final int position) {
         FileData curOne= curFiles.get(position);
         switch (parseType(curOne.name)){
             case 0:{
                 holder.typeImage.setImageResource(R.drawable.ic_folder_black_24dp);
                 break;
             }
             case 1:{
                 holder.typeImage.setImageResource(R.drawable.ic_spike_design_guitar_svgrepo_com);
                 break;
             }
             case 2:{
                 holder.typeImage.setImageResource(R.drawable.ic_photo_svgrepo_com);
                 break;
             }
             case 3:{
                 holder.typeImage.setImageResource(R.drawable.ic_files_pngrepo_com);
                 break;
             }
         }
         holder.nameTextView.setText(curOne.name);

            holder.typeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Тут ты пытаешься скачать файл"+ curFiles.get(position).name,Toast.LENGTH_SHORT).show();
                }
            });

         holder.menuButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                 View bottomSheetView = LayoutInflater.from(context).inflate(
                         R.layout.bottom_sheet_layout,
                         (LinearLayout) v.findViewById(R.id.bottomShit)
                 );
                 bottomSheetView.findViewById(R.id.OpenFolder).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Open file"+ curFiles.get(position).name,Toast.LENGTH_SHORT).show();
                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetView.findViewById(R.id.Rename).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Rename file"+ curFiles.get(position).name,Toast.LENGTH_SHORT).show();
                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetView.findViewById(R.id.Move).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Move file"+ curFiles.get(position).name,Toast.LENGTH_SHORT).show();
                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetView.findViewById(R.id.Prop).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Propertoes file"+ curFiles.get(position).name,Toast.LENGTH_SHORT).show();
                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetDialog.setContentView(bottomSheetView);
                 bottomSheetDialog.show();
             }

         });

    }

    @Override
    public int getItemCount() {
        return curFiles.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<FileData> sortedData=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                sortedData.addAll(allFiles);
            }else{
                String sortPattern=constraint.toString().toLowerCase().trim();
                for(FileData item: allFiles){
                    if(item.name.toLowerCase().trim().contains(sortPattern)){
                        sortedData.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=sortedData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            curFiles.clear();
            curFiles.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };







    static class itemViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        ImageView typeImage;
        private ImageButton menuButton;

        itemViewHolder(@NonNull View itemView) {
            super(itemView);

            menuButton=itemView.findViewById(R.id.menuButton);
            nameTextView=itemView.findViewById(R.id.fileName);
            typeImage=itemView.findViewById(R.id.iconView);


        }
    }

    private static int parseType(String str){
        String extension="";
        int i=str.lastIndexOf('.');
        if(i==-1){
            return 0;
        }else{
            extension=str.substring(i+1);
            if(extension.equals("mp3") || extension.equals("flac") || extension.equals("wav") || extension.equals("alac") || extension.equals("aiff"))
                return 1;
            if(extension.equals("png") || extension.equals("bmp") || extension.equals("jpg")|| extension.equals("gif")|| extension.equals("tiff")|| extension.equals("raw")|| extension.equals("psd"))
                return 2;
            if(extension.equals("txt") || extension.equals("doc") || extension.equals("pdf")|| extension.equals("docx")|| extension.equals("xls")|| extension.equals("ptt")|| extension.equals("xlsx"))
                return 3;
        }
        return -1;
    }

}
