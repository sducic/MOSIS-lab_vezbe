package elfak.mosis.zeljko.myplaces;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class EditMyPlaceActivity extends AppCompatActivity implements View.OnClickListener {

    boolean editMode=true;
    int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_place);
        try{
            Intent listIntent=getIntent();
            Bundle positionBundle=listIntent.getExtras();
            if(positionBundle != null)
                position=positionBundle.getInt("position");
            else
                editMode=false;
        }   catch(Exception e)  { editMode=false;}

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final Button finishedButton=(Button)findViewById(R.id.editmyplace_finished_button);
        Button canceledButton = (Button)findViewById(R.id.editmyplace_cancel_button);
        finishedButton.setOnClickListener(this);
        finishedButton.setEnabled(false);
        finishedButton.setText("Add");
        Button cancelButton=(Button)findViewById(R.id.editmyplace_cancel_button);
        cancelButton.setOnClickListener(this);
        EditText nameEditText=(EditText)findViewById(R.id.editmyplace_name_edit);
        EditText descEditText = (EditText)findViewById(R.id.editmyplace_desc_edit);
        EditText latitudeEdit = (EditText)findViewById((R.id.editmyplace_lat_edit));
        EditText longitudeEdit = (EditText)findViewById((R.id.editmyplace_lon_edit));
        if(!editMode)
        {
            finishedButton.setEnabled(false);
            finishedButton.setText("Add");

        } else if(position>=0)
        {
            finishedButton.setText("Save");
            MyPlace place=MyPlacesData.getInstance().getPlace(position);
            nameEditText.setText(place.name);
            descEditText.setText(place.description);

            latitudeEdit.setText(place.latitude);

            longitudeEdit.setText((place.longitude));
        }

        finishedButton.setOnClickListener(this);
        finishedButton.setEnabled(false);
        canceledButton.setOnClickListener(this);


        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length()>0);
            }
        });

        descEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length()>0);
            }
        });


        latitudeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length()>0);
            }
        });


        longitudeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                finishedButton.setEnabled(s.length()>0);
            }
        });


        Button locationButton = (Button)findViewById(R.id.editmyplace_location_button);
        locationButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editmyplace_finished_button: {
                EditText etName = (EditText) findViewById(R.id.editmyplace_name_edit);
                String nme = etName.getText().toString();
                EditText etDesc = (EditText) findViewById(R.id.editmyplace_desc_edit);
                String desc = etDesc.getText().toString();
                EditText latEdit = (EditText)findViewById(R.id.editmyplace_lat_edit);
                String lat = latEdit.getText().toString();
                EditText lonEdit = (EditText)findViewById(R.id.editmyplace_lon_edit);
                String lon = lonEdit.getText().toString();
                if(!editMode){
                    MyPlace place=new MyPlace(nme,desc);
                    place.latitude=lat;
                    place.longitude=lon;
                    MyPlacesData.getInstance().addNewPlace(place);
                } else{
                   MyPlacesData.getInstance().updatePlace(position,nme,desc,lon,lat);
                }
                setResult(Activity.RESULT_OK);
                finish();
                break;
            }
            case R.id.editmyplace_cancel_button: {
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            }
            case R.id.editmyplace_location_button:
            {
                Intent i = new Intent(this,MyPlacesMapsActivity.class);
                ///
                i.putExtra("state", MyPlacesMapsActivity.SELECT_COORDINATES);
                ///
                startActivityForResult(i,1);
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(resultCode == Activity.RESULT_OK) {
                String lon = data.getExtras().getString("lon");
                EditText lonText = (EditText) findViewById(R.id.editmyplace_lon_edit);
                lonText.setText(lon);
                String lat = data.getExtras().getString("lat");
                EditText latText = (EditText) findViewById(R.id.editmyplace_lat_edit);
                latText.setText(lat);
            }
        }
        catch(Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_my_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.show_map_item) {
            Toast.makeText(this,"Show Map!", Toast.LENGTH_SHORT).show();

        }
        else if (id == R.id.my_places_list_item) {

            Intent i = new Intent(this, MyPlacesList.class);
            startActivity(i);

        } else if(id == R.id.about_item) {

            Intent i = new Intent(this,About.class);
            startActivity(i);
        }
        else if(id==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
