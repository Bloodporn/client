package com.example.client;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Files.FileData;
import com.example.client.Files.Tree;
import com.example.client.Files.TreeItem;
import com.example.client.connection.NetworkServiceFileDownload;
import com.example.client.connection.NetworkServiceFileUpload;
import com.example.client.connection.Request;
import com.example.client.connection.Response;
import com.example.client.dataclient.DataClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class FolderView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recadapter;
    SqlService sql;
    Tree tree;
    FloatingActionButton fab;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        sql = new SqlService(this);

        int permissionCheck = ContextCompat.checkSelfPermission(FolderView.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FolderView.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 26);
        }

        //File storage = new File("/Bolt");
        //if(!storage.exists())
            //Log.i("file: ", Boolean.toString(storage.mkdirs()));

        String dirPath = getFilesDir().getAbsolutePath() + File.separator + "newfoldername";
        File projDir = new File(dirPath);
        if (!projDir.exists())
            Log.i("file: ", Boolean.toString(projDir.mkdirs()));

        final ActionBar actionBar= getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navigbar)));

        Window window=this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ExternalFunc.setStatusBarGradiant(this,R.color.navigbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }

        tree = new Tree();
        if (DataClient.tree != null) {
            Pattern pattern = Pattern.compile("\n");
            parseTree(tree.getRoot(), pattern.split(DataClient.tree), new Index(0), 0);
        }


        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        final View all = findViewById(R.id.home);
        final View fav = findViewById(R.id.important);
        bottomNavigationView.setSelectedItemId(R.id.home);
        all.setAlpha(1);
        fav.setAlpha(0.8f);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.navigbar));

        recyclerView=findViewById(R.id.review);
        int resId1 = R.anim.anim1_layout;
        int resId2 = R.anim.anim2_lauout;
        final LayoutAnimationController animation1 = AnimationUtils.loadLayoutAnimation(this,resId1);
        final LayoutAnimationController animation2 = AnimationUtils.loadLayoutAnimation(this,resId2);



        /*bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:{
                        if(!isActive1){
                            all.setAlpha(1);
                            fav.setAlpha(0.8f);
                            recadapter.swithcData(files);
                            recyclerView.setLayoutAnimation(animation2);
                            recyclerView.scheduleLayoutAnimation();
                            actionBar.setTitle("All of your data");
                            isActive2=false;
                            isActive1=true;
                            return true;
                        }
                        return false;
                    }
                    case R.id.important:{
                        if(!isActive2){
                            all.setAlpha(0.8f);
                            fav.setAlpha(1);
                            recadapter.swithcData(files);
                            recyclerView.setLayoutAnimation(animation1);
                            recyclerView.scheduleLayoutAnimation();
                            actionBar.setTitle("All of important your data");
                            isActive1=false;
                            isActive2=true;
                            return true;
                        }
                       return false;
                    }
                }
                return false;
            }
        });*/





        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recadapter=new RecyclerAdapter(tree,this, getSupportActionBar());
        recyclerView.setAdapter(recadapter);
        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(FolderView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FolderView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 25);
                } else {
                    upload();
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0 || dx<0 && fab.isShown()){
                    fab.hide();
                }
            }

        });



    }
    SearchView searchView;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 25:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    upload();
                }
            case 26:
                if (!((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)))
                break;
            default:
                break;
        }
    }

    private void upload() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(i,"Выберите файл"), 9999);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //recadapter.getFilter().filter(newText);
                return true;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    private long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    private String getPath(Context context, Uri uri) throws URISyntaxException {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 9999:
                String path = null;
                try {
                    path = getPath(FolderView.this, data.getData());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                File file = new File(path);
                String filename = file.getName();
                if (file.isDirectory()) {
                    Request request = new Request(
                            "UPLOAD",
                            tree.getPath() + file.getName() +
                                    "//" + getFolderSize(file) + "//0",
                            200);
                    UploadFile uploadFile = new UploadFile(file, !file.isDirectory(), request);
                    uploadFile.execute();
                } else {
                    Request request = new Request(
                            "UPLOAD",
                            tree.getPath() + file.getName() +
                                    "//" + file.length() + "//1",
                            200);
                    UploadFile uploadFile = new UploadFile(file, !file.isDirectory(), request);
                    uploadFile.execute();
                }
                break;
        }
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recadapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                recadapter.goToParent();
                if (recadapter.isRoot()) {
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        getSupportActionBar().setHomeButtonEnabled(false);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class Index
    {
        public int index;

        public Index(int index)
        {
            this.index = index;
        }
    }

    private void parseTree(TreeItem root, String[] strings, Index index, int rank) {
        Pattern pattern;

        TreeItem newRoot = root;
        while (index.index != strings.length) {
            pattern = Pattern.compile("\t");
            String[] stringsItem = pattern.split(strings[index.index]);
            if (Integer.parseInt(stringsItem[0]) > rank) {
                parseTree(newRoot, strings, index, rank + 1);
                continue;
            } else if (Integer.parseInt(stringsItem[0]) < rank) {
                return;
            } else {
                pattern = Pattern.compile("\\\\");
                String[] regex = pattern.split(stringsItem[2]);
                boolean type = regex[0].equals("-1") ? true : false;
                String size = regex[0].equals("-1") ? "0" : regex[0];
                TreeItem newItem = null;
                regex[1] = regex[1].replace("T", " ");
                regex[1] = regex[1].substring(0, regex[1].length() - 8);
                newItem = new TreeItem(new FileData(type, stringsItem[1], regex[1], Long.parseLong(size)), root);
                root.getChildren().add(newItem);
                newRoot = newItem;
            }
            index.index++;
        }
    }


    class UploadFile extends NetworkServiceFileUpload {

        private File fileThis;

        public UploadFile(File file, boolean isFile, Request request) {
            super(file, isFile, request);
            this.fileThis = file;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isValidCode()) {
                Toast.makeText(FolderView.this, response.toString(), Toast.LENGTH_SHORT).show();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (isFile) {
                    TreeItem newFile = new TreeItem(new FileData(
                            false,
                            fileThis.getName(),
                            dateFormat.format(new Date(fileThis.lastModified())),
                            fileThis.length()), tree.getCur());
                    tree.getCur().getChildren().add(newFile);
                    DataClient.storageFill += fileThis.length();
                } else {

                    TreeItem newFile = new TreeItem(new FileData(false,
                            fileThis.getName(),
                            dateFormat.format(new Date()),
                            0),tree.getCur());
                    tree.getCur().getChildren().add(newFile);
                    recTree(fileThis, newFile);
                }
                recadapter.update();
            } else {
                Toast.makeText(FolderView.this, "Не успешно", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            long inMoment = values[0];
            long allSize = values[1];
            //TODO: Добавить прогресс.
        }

        protected void recTree(File fileSource,TreeItem parent) {
            for (File file : fileSource.listFiles()) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (file.isDirectory()) {
                    TreeItem newPath = new TreeItem(new FileData(
                            true,
                            file.getName(),
                            dateFormat.format(new Date(file.lastModified())),
                            0), parent);
                    parent.getChildren().add(newPath);
                    recTree(file,newPath);
                } else {
                    TreeItem newFile = new TreeItem(new FileData(
                            false,
                            file.getName(),
                            dateFormat.format(new Date(file.lastModified())),
                            file.length()), parent);
                    parent.getChildren().add(newFile);
                    DataClient.storageFill += file.length();
                }
            }
        }
    }



}
