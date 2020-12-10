package com.kitchenworks.chefclient;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static Thread threadnames;
    AutoCompleteTextView CTDropdown;
    TextView textView;
    TextInputLayout inputLayout;
    MaterialButton button;
    StringBuffer response = new StringBuffer();
    String empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.threadnames = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL obj = new URL ( getString(R.string.server)+"chef/getusersmobile/");
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                    } else {
                        System.out.println("GET request not worked");
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });threadnames.start();

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.inicial_activity_main);

        while(threadnames.isAlive());

        JSONArray arr = null;
        List<String> list = new ArrayList<String>();
        try {
            arr = new JSONArray(response.toString());
            for(int i = 0; i < arr.length(); i++){
                String str = arr.getJSONObject(i).getString("nombre");
                list.add(str);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] empresas = list.toArray(new String[0]);

        CTDropdown = findViewById(R.id.contract_dropdown);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        empresas);
        CTDropdown.setAdapter(adapter);

        inputLayout = findViewById(R.id.dropdown);
        textView = findViewById(R.id.selecionct);
        ViewTreeObserver vto = inputLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inputLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = inputLayout.getMeasuredWidth();
                textView.setWidth(width);
            }
        });

        button = findViewById(R.id.btnIni);
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                //new int[] {-android.R.attr.state_checked}, // unchecked
                //new int[] { android.R.attr.state_pressed}  // pressed
        };
        int[] colors = new int[] {
                Color.WHITE,
                Color.WHITE
        };
        ColorStateList myList = new ColorStateList(states, colors);
        button.setStrokeColor(myList);
        button.setEnabled(false);
        button.setBackgroundColor(Color.parseColor("#2a295c"));
        button.setTextColor(Color.parseColor("#FFFFFF"));

        CTDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                empresa = s.toString();
                final String empresar = StringUtils.stripAccents(empresa).replaceAll(" ","-");
                Thread threadimg;
                final ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
                //final String ruta = contextWrapper.getFilesDir() + "/"+ empresar + "Menu.jpg";
                final String ruta = contextWrapper.getFilesDir() + "/"+ "prueba/Menu0.jpg";
                final Boolean finish = false;

                while(!finish){
                    threadimg = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //URL url = new URL ( getString(R.string.server)+empresar+"Menu.jpg");
                                URL url = new URL ( getString(R.string.server)+empresar+"prueba/"+"Menu8.jpg");
                                InputStream input = url.openStream();

                                //File mydir = contextWrapper.getDir("prueba", Context.MODE_PRIVATE); //Creating an internal dir;
                                //System.out.println("dir "+mydir);
                                //File fileWithinMyDir = new File(mydir, "Menu0.jpg"); //Getting a file within the dir.
                                //FileOutputStream output = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
                                //FileOutputStream s = new FileOutputStream(file,false);

                                File mFileTemp = new File(contextWrapper.getFilesDir() + File.separator
                                        + "prueba"
                                        , "Menu0"
                                        + ".jpg");
                                mFileTemp.getParentFile().mkdirs();
                                FileOutputStream output = new FileOutputStream(mFileTemp,false);

                                //OutputStream output = new FileOutputStream(ruta,false);
                                try {
                                    byte[] buffer = new byte[64*1024];
                                    int bytesRead = 0;
                                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                                        output.write(buffer, 0, bytesRead);
                                    }
                                } finally {
                                    output.close();
                                }
                                input.close();
                            }catch (MalformedURLException e){
                                e.printStackTrace();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        /*OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(getString(R.string.server)+"chef/prueba")
                                .build();

                        try (Response response = client.newCall(request).execute()) {
                            System.out.println(response.body().string());
                            response.body().
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        }
                    });threadimg.start();
                }


                button.setEnabled(true);
                button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                button.setTextColor(Color.parseColor("#2a295c"));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Second_activity.class);
                intent.putExtra("empresa", empresa);
                v.getContext().startActivity(intent);
            }
        });
    }
}
