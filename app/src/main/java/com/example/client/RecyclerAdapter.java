package com.example.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder>{
    private List<FileData> files;
    private Context context;

    public RecyclerAdapter(ArrayList<FileData> files, Context context) {
        this.files = files;
        this.context = context;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new itemViewHolder(LayoutInflater.from(context).inflate(R.layout.storage_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final itemViewHolder holder, final int position) {
         FileData curOne=files.get(position);
         switch (parseType(curOne.getDiskPath())){
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
         holder.nameTextView.setText(curOne.getName());



         holder.menuButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 /*
                 PopupMenu popupMenu=new PopupMenu(context,holder.menuButton);
                 popupMenu.inflate(R.menu.file_menu);
                 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         switch (item.getItemId()){
                             case R.id.menu_item_download:{
                                 Toast.makeText(context,"dowload pressed" + holder.nameTextView.getText(),Toast.LENGTH_SHORT).show();
                                 break;
                             }
                             case R.id.menu_item_remove:{
                                 Toast.makeText(context,"remove pressed" + holder.nameTextView.getText(),Toast.LENGTH_SHORT).show();
                                 break;
                             }
                         }
                         return false;
                     }
                 });
                 popupMenu.show();
                 */
                 final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                 View bottomSheetView = LayoutInflater.from(context).inflate(
                         R.layout.bottom_sheet_layout,
                         (LinearLayout) v.findViewById(R.id.bottomShit)
                 );
                 bottomSheetView.findViewById(R.id.OpenFolder).setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Toast.makeText(context,"Open file"+ files.get(position).getName(),Toast.LENGTH_SHORT).show();
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
        return files.size();
    }

    class itemViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        ImageView typeImage;
        private ImageButton menuButton;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            menuButton=itemView.findViewById(R.id.menuButton);
            nameTextView=itemView.findViewById(R.id.fileName);
            typeImage=itemView.findViewById(R.id.iconView);


        }
    }

    static public int parseType(String str){
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
