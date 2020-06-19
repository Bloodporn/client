package com.example.client;

import android.content.Context;
import android.os.Environment;
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
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Files.FileData;
import com.example.client.Files.Tree;
import com.example.client.Files.TreeItem;
import com.example.client.connection.DownloadFile;
import com.example.client.connection.Request;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> implements Filterable {

    private Context context;
    private Tree tree;
    private ArrayList<FileData> cur;
    private ActionBar actionBar;

    RecyclerAdapter(Tree files, Context context, ActionBar actionBar) {
        this.context = context;
        this.tree = files;
        this.actionBar = actionBar;
        cur = new ArrayList<>();
        for (TreeItem item: tree.getCur().getChildren()) {
            cur.add(item.getValue());
        };
    }

    public Tree getTree() {
        return tree;
    }


    public void goToParent() {
        if (!isRoot()) {
            tree.setCur(tree.getCur().getParent());
            notifyDataSetChanged();
        }
    }

    public boolean isRoot() {
        return tree.getCur().getParent() == null;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemViewHolder(LayoutInflater.from(context).inflate(R.layout.storage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder holder, final int position) {
         FileData curOne= tree.getCur().getChildren().get(position).getValue();
         switch (parseType(curOne.name)){
             case 0:{
                 holder.typeImage.setImageResource(R.drawable.ic_folder_black_24dp);
                 holder.typeImage.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         tree.setCur(tree.getCur().getChildren().get(position));
                         if (!isRoot()) {
                             if (actionBar != null) {
                                 actionBar.setDisplayHomeAsUpEnabled(true);
                                 actionBar.setHomeButtonEnabled(true);
                             }
                         }
                         notifyDataSetChanged();
                     }
                 });
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
                 Request request = new Request(
                         "UPLOAD",
                         tree.getPath() + tree.getCur().getChildren().get(position).getValue().name,
                         201);
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
                         Toast.makeText(context,"Open file"+ tree.getCur().getChildren().get(position).getValue().name,Toast.LENGTH_SHORT).show();

                         Request request = new Request(
                                 "UPLOAD",
                                 tree.getPath() + tree.getCur().getChildren().get(position).getValue().name,
                                 201);
                         DownloadFile dowl= new DownloadFile(new File(Environment.getDataDirectory() + File.separator + "BOLT"),true,request,context);


                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetView.findViewById(R.id.Rename).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Rename file"+ tree.getCur().getChildren().get(position).getValue().name,Toast.LENGTH_SHORT).show();
                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetView.findViewById(R.id.Move).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Move file"+ tree.getCur().getChildren().get(position).getValue().name,Toast.LENGTH_SHORT).show();
                         bottomSheetDialog.dismiss();
                     }
                 });
                 bottomSheetView.findViewById(R.id.Prop).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Propertoes file"+ tree.getCur().getChildren().get(position).getValue().name,Toast.LENGTH_SHORT).show();
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
        return tree.getCur().getChildren().size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TreeItem> sortedData=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                sortedData.addAll(tree.getCur().getChildren());
            }else{
                String sortPattern=constraint.toString().toLowerCase().trim();
                for(TreeItem item: tree.getCur().getChildren()){
                    if(item.getValue().name.toLowerCase().trim().contains(sortPattern)){
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
            tree.getCur().getChildren().clear();
            tree.getCur().getChildren().addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public void update() {
        notifyDataSetChanged();
    }


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
