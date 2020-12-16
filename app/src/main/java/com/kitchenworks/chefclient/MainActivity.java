package com.kitchenworks.chefclient;

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Thread threadnames;
    AutoCompleteTextView CTDropdown;
    TextView textView;
    TextInputLayout inputLayout;
    MaterialButton button;
    StringBuffer response = new StringBuffer();
    StringBuffer response2 = new StringBuffer();
    String empresa, AdminName;
    int imgnummenu, imgnumtips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //SE REALIZA LA PETICION PARA OBTENER EL LISTADO DE USUARIOS

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

        //CUANDO SE SELECCIONE UN RESTAURANTE DE LA LISTA DESPLEGABLE
        CTDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                empresa = s.toString();
                final String empresar = StringUtils.stripAccents(empresa).replaceAll(" ","-");
                Thread threadimgnum, threadimg;

                //REALIZA LA CONSULTA DEL NUMERO DE IMAGENES DE MENÚ PARA EL RESTAURANTE SELECCIONADO
                threadimgnum = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL obj = new URL ( getString(R.string.server)+"chef/getimgnum/"+empresa);
                            HttpURLConnection con2 = (HttpURLConnection) obj.openConnection();
                            con2.setRequestMethod("GET");
                            int responseCode2 = con2.getResponseCode();
                            if (responseCode2 == HttpURLConnection.HTTP_OK) { // success
                                BufferedReader in2 = new BufferedReader(new InputStreamReader(
                                        con2.getInputStream()));
                                String inputLine2;
                                while ((inputLine2 = in2.readLine()) != null) {
                                    response2.append(inputLine2);
                                }
                                in2.close();
                            } else {
                                System.out.println("GET request not worked");
                            }
                        }catch (MalformedURLException e){
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });threadimgnum.start();

                while(threadimgnum.isAlive());

                JSONArray arr2 = null;
                imgnummenu =0;
                try {
                    arr2 = new JSONArray(response2.toString());
                    imgnummenu = Integer.parseInt(arr2.getJSONObject(0).getString("imgnum"));
                    imgnumtips = Integer.parseInt(arr2.getJSONObject(1).getString("imgnum"));
                    AdminName = arr2.getJSONObject(2).getString("nombre");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("Datos de valor: imgnummenu: "+imgnummenu+"imgnumtips: "+imgnumtips+"Admin: "+AdminName);
                final ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

                //SI EL NUMERO DE IMAGENES DE TIS PARA EL RESTAURANTE NO ES CERO ENTONCES TRAE LAS IMAGENES Y LAS GUARDA
                if(imgnumtips >0){
                    for(int j = 0; j< imgnummenu; j++){
                        final int index = j;
                        threadimg = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL ( getString(R.string.server)+AdminName+"/Menu"+index+".jpg");
                                    InputStream input = url.openStream();
                                    File mFileTemp = new File(contextWrapper.getFilesDir() + File.separator
                                            + AdminName
                                            , "Menu"
                                            + index
                                            + ".jpg");
                                    mFileTemp.getParentFile().mkdirs();
                                    FileOutputStream output = new FileOutputStream(mFileTemp,false);

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
                                }catch (FileNotFoundException e){
                                    //error
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        });threadimg.start();
                    }
                }else {

                }

                //SI EL NUMERO DE IMAGENES DE MENU PARA EL RESTAURANTE NO ES CERO ENTONCES TRAE LAS IMAGENES Y LAS GUARDA
                if(imgnummenu >0){
                    for(int j = 0; j< imgnummenu; j++){
                        final int index = j;
                        threadimg = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL ( getString(R.string.server)+empresar+"/Menu"+index+".jpg");
                                    InputStream input = url.openStream();
                                    File mFileTemp = new File(contextWrapper.getFilesDir() + File.separator
                                            + empresar
                                            , "Menu"
                                            + index
                                            + ".jpg");
                                    mFileTemp.getParentFile().mkdirs();
                                    FileOutputStream output = new FileOutputStream(mFileTemp,false);

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
                                }catch (FileNotFoundException e){
                                    //error
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        });threadimg.start();
                    }
                }else{
                    //renderizar pantalla no se encontraron imagenes...
                    System.out.println("no está entrando");
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
                intent.putExtra("imgnummenu", imgnummenu);
                intent.putExtra("imgnumtips", imgnumtips);
                intent.putExtra("admin", AdminName);
                v.getContext().startActivity(intent);
            }
        });
    }
}
