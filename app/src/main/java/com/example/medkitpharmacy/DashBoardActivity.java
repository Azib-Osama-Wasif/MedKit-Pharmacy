package com.example.medkitpharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    TextView textView;
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    RecyclerView rcv;
    myadapter adapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        textView = (TextView)findViewById(R.id.signuplogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLoginActivity();
            }

            private void moveToLoginActivity() {
                Intent intent=new Intent(com.example.medkitpharmacy.DashBoardActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });


        ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.drugs,"Capsules"));
        item.add(new StaticRvModel(R.drawable.syrup,"Syrup"));
        item.add(new StaticRvModel(R.drawable.syringe,"Injections"));
        item.add(new StaticRvModel(R.drawable.bandaid,"FirstAid"));



        recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(staticRvAdapter);

        setTitle("MEDkit");

        rcv=(RecyclerView)findViewById(R.id.recview);
        // rcv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new myadapter(dataqueue());
        rcv.setAdapter(adapter);

        // LinearLayoutManager layoutManager =new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        // rcv.setLayoutManager(layoutManager);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        rcv.setLayoutManager(gridLayoutManager);



        //for sidebar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navi_view);
        //------------------Navigation Drawer menu---------------
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_home);
        //-----------hide any item from navigation bar---------
//        Menu menu = navigationView.getMenu();
//        menu.findItem(R.id.nav_address).setVisible(false);

    }


    public ArrayList<DynamicRVModel> dataqueue()
    {
        ArrayList<DynamicRVModel> holder=new ArrayList<>();

        DynamicRVModel ob1=new DynamicRVModel();
        ob1.setHeader("Panadol Child");
        ob1.setDesc("This product is contraindicated in patients with a previous history of hypersensitivity to paracetamol or excipients");
        ob1.setPrice("Rs.100");
        ob1.setImgname(R.drawable.panadol);
        holder.add(ob1);

        DynamicRVModel ob2=new DynamicRVModel();
        ob2.setHeader("Tummy Soft");
        ob2.setDesc("This product is contraindicated in patients with a previous history of hypersensitivity to paracetamol or excipients");
        ob2.setPrice("Rs.75");
        ob2.setImgname(R.drawable.tummysoft);
        holder.add(ob2);

        DynamicRVModel ob3=new DynamicRVModel();
        ob3.setHeader("Benylin chesty");
        ob3.setDesc("The best cough medicine, however, will depend on your symptoms");
        ob3.setPrice("Rs.75");
        ob3.setImgname(R.drawable.benylinchesty);
        holder.add(ob3);

        DynamicRVModel ob4=new DynamicRVModel();
        ob4.setHeader("Co-Ex-Cough");
        ob4.setDesc("The best cough medicine, however, will depend on your symptoms");
        ob4.setPrice("Rs.95");
        ob4.setImgname(R.drawable.coexcough);
        holder.add(ob4);

        DynamicRVModel ob5=new DynamicRVModel();
        ob5.setHeader("Headache Relief");
        ob5.setDesc("This product is contraindicated in patients with a previous history of hypersensitivity to paracetamol or excipients");
        ob5.setPrice("Rs.50");
        ob5.setImgname(R.drawable.constipationreliefsyrup);
        holder.add(ob5);

        DynamicRVModel ob6=new DynamicRVModel();
        ob6.setHeader("Pain Relief");
        ob6.setDesc("This product is contraindicated in patients with a previous history of hypersensitivity to paracetamol or excipients");
        ob6.setPrice("Rs.50");
        ob6.setImgname(R.drawable.constipationreliefsyrup);
        holder.add(ob6);

        DynamicRVModel ob7=new DynamicRVModel();
        ob7.setHeader("Tummy Soft");
        ob7.setDesc("This product is contraindicated in patients with a previous history of hypersensitivity to paracetamol or excipients");
        ob7.setPrice("Rs.75");
        ob7.setImgname(R.drawable.tummysoft);
        holder.add(ob7);

        DynamicRVModel ob8=new DynamicRVModel();
        ob8.setHeader("Benylin chesty");
        ob8.setDesc("The best cough medicine, however, will depend on your symptoms");
        ob8.setPrice("Rs.75");
        ob8.setImgname(R.drawable.benylinchesty);
        holder.add(ob8);

        DynamicRVModel ob9=new DynamicRVModel();
        ob9.setHeader("Benylin chesty");
        ob9.setDesc("The best cough medicine, however, will depend on your symptoms");
        ob9.setPrice("Rs.75");
        ob9.setImgname(R.drawable.benylinchesty);
        holder.add(ob9);

        DynamicRVModel ob10=new DynamicRVModel();
        ob10.setHeader("Co-Ex-Cough");
        ob10.setDesc("The best cough medicine, however, will depend on your symptoms");
        ob10.setPrice("Rs.95");
        ob10.setImgname(R.drawable.coexcough);
        holder.add(ob10);



        return holder;

    }
    //for handle back button of sidebar
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                break;
            case R.id.menu_profile:
                //for switch to upload_image page
                Toast.makeText(this,"Menu Profile Pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_nearby:
                Toast.makeText(this,"Menu NearBy pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                Toast.makeText(this,"Menu Settings pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                Toast.makeText(this,"LogOut pressed", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}