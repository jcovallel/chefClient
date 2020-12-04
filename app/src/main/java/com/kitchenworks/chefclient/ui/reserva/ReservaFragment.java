package com.kitchenworks.chefclient.ui.reserva;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.kitchenworks.chefclient.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.kitchenworks.chefclient.Second_activity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservaFragment extends Fragment {

    private ReservaViewModel reservaViewModel;
    private TextInputEditText nombre,celular,email,cargo,direccion, observaciones;
    private String fecha,hora,empresasend,empresaget;
    private AutoCompleteTextView editTextFilledExposedDropdown, editTextFilledExposedDropdown2, editTextFilledExposedDropdown3, editTextFilledExposedDropdown4;
    TextInputLayout horaContainer, dirContainer, diaContainer, entregaContainer, menuContainer;
    Button btnEnviar;
    private TextView textView;
    private ImageView imageView;
    private ScrollView scrollView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        Second_activity activity = (Second_activity) getActivity();
        empresaget = activity.getEmpresa().replaceAll(" ", "%20");
        empresasend = activity.getEmpresa();

        View root = inflater.inflate(R.layout.fragment_reserva, container, false);

        /*AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());
        asyncLayoutInflater.inflate(R.layout.fragment_reserva, container, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resid, @Nullable ViewGroup parent) {
                parent.addView(view);
                onViewCreated(view, savedInstanceState);
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);*/

        reservaViewModel = ViewModelProviders.of(this).get(ReservaViewModel.class);

        scrollView = root.findViewById(R.id.scrollView);
        imageView = root.findViewById(R.id.imageView);
        textView = root.findViewById(R.id.textView);

        scrollView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

        Thread threadone;
        final StringBuffer responsef = new StringBuffer();
        threadone = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL obj = new URL ( getString(R.string.server)+"chef/getdispodiassitio/"+empresaget);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            responsef.append(inputLine);
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
        });threadone.start();
        while(threadone.isAlive());
        JSONArray arr = null;
        List<String> list = new ArrayList<String>();
        final int dianumb = Integer.parseInt(new SimpleDateFormat("u", Locale.getDefault()).format(new Date()));
        int horaint = Integer.parseInt(new SimpleDateFormat("k", Locale.getDefault()).format(new Date()));
        int min = Integer.parseInt(new SimpleDateFormat("m", Locale.getDefault()).format(new Date()));
        System.out.println("dias: "+dianumb);
        try {
            arr = new JSONArray(responsef.toString());
            if((Boolean)arr.getJSONObject(0).get("lunes") && dianumb<=1){
                list.add("Lunes");
            }
            if((Boolean)arr.getJSONObject(0).get("martes") && dianumb<=2){
                list.add("Martes");
            }
            if((Boolean)arr.getJSONObject(0).get("miercoles") && dianumb<=3){
                list.add("Miercoles");
            }
            if((Boolean)arr.getJSONObject(0).get("jueves") && dianumb<=4){
                list.add("Jueves");
            }
            if((Boolean)arr.getJSONObject(0).get("viernes") && dianumb<=5){
                System.out.println("im here");
                list.add("Viernes");
            }
            if((Boolean)arr.getJSONObject(0).get("sabado") && dianumb<=6){
                list.add("Sabado");
            }
            if((Boolean)arr.getJSONObject(0).get("domingo") && dianumb<=0){
                list.add("Domingo");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(list.toString());
        //boolean nocturno = false;
        /*if(empresasend.equals("Albahaca") && dianumb.equals("5") && horaint<=19 && horaint>=18){
            if(!(horaint==19 && min>45)){
                nocturno=true;
            }
        }
        final String [] Adias;
        if(list.toArray(new String[0]).length==0 && nocturno){
            Adias = new String[] {"Viernes"};
        }else{
            Adias = list.toArray(new String[0]);
        }*/
        final String [] Adias;
        Adias = list.toArray(new String[0]);
        if(Adias.length>0){
            scrollView.setVisibility(View.VISIBLE);
            btnEnviar = root.findViewById(R.id.enviar);
            btnEnviar.setEnabled(false);
            CheckBox mcheck = root.findViewById(R.id.checkBox);

            nombre = root.findViewById(R.id.nombreRe);
            celular = root.findViewById(R.id.celularRe);
            email = root.findViewById(R.id.correoRe);
            cargo = root.findViewById(R.id.CargoRe);
            editTextFilledExposedDropdown4 = root.findViewById(R.id.tmenu_dropdown);
            editTextFilledExposedDropdown = root.findViewById(R.id.filled_exposed_dropdown);
            editTextFilledExposedDropdown2 = root.findViewById(R.id.filled_exposed_dropdown_entrega);
            editTextFilledExposedDropdown3 = root.findViewById(R.id.filled_exposed_dropdown_hora);
            direccion = root.findViewById(R.id.direccion);
            horaContainer = root.findViewById(R.id.contenedor_hora);
            dirContainer = root.findViewById(R.id.addressContainer);
            diaContainer = root.findViewById(R.id.contenedor_dia);
            entregaContainer = root.findViewById(R.id.contenedor_entrega);
            menuContainer = root.findViewById(R.id.contenedor_menu);
            menuContainer.setVisibility(View.GONE);
            horaContainer.setVisibility(View.GONE);
            dirContainer.setVisibility(View.GONE);
            entregaContainer.setVisibility(View.GONE);
            observaciones = root.findViewById(R.id.ObservacionesRe);

            DateFormat fechaformat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat horaformat = new SimpleDateFormat("HH:mm");
            Date dateobj = new Date();
            fecha=fechaformat.format(dateobj);
            hora=horaformat.format(dateobj);

            TextView politicatext = root.findViewById(R.id.politica);
            String text = "Sus datos personales han sido y están siendo tratados conforme" +
                    " con nuestra Política de Tratamiento de Datos Personales. Para mayor " +
                    "información podrá consultar nuestra política en la página web: Política " +
                    "de Privacidad";
            SpannableString ss = new SpannableString(text);

            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://co.sodexo.com/files/live/sites/com-co/files/Politicas_de_privacidad.pdf"));
                    startActivity(viewIntent);
                }
            };

            ss.setSpan(clickableSpan1,193,215, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            politicatext.setText(ss);
            politicatext.setMovementMethod(LinkMovementMethod.getInstance());

            mcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                    if(isChecked){
                        btnEnviar.setEnabled(true);
                    }else{
                        btnEnviar.setEnabled(false);
                    }
                }
            });

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(
                            getContext(),
                            R.layout.dropdown_menu_popup_item,
                            Adias);
            editTextFilledExposedDropdown.setAdapter(adapter);

            editTextFilledExposedDropdown.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String [] menus;
                    Thread threadst;
                    final StringBuffer response2 = new StringBuffer();
                    threadst = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                URL obj = new URL ( getString(R.string.server)+"chef/getmenustrueempresa/"+empresaget);
                                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                con.setRequestMethod("GET");
                                int responseCode = con.getResponseCode();
                                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                            con.getInputStream()));

                                    String inputLine;
                                    while ((inputLine = in.readLine()) != null) {
                                        response2.append(inputLine);
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
                    });threadst.start();
                    while(threadst.isAlive());
                    JSONArray arr = null;
                    List<String> list = new ArrayList<String>();
                    try {
                        arr = new JSONArray(response2.toString());
                        for(int i = 0; i < arr.length(); i++){
                            String str = arr.getJSONObject(i).getString("menu");
                            list.add(str);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    menus = list.toArray(new String[0]);
                    if(menus.length==0){
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Lo Sentimos.")
                                .setMessage("No se encontraron menus para el restaurante selecccionado, " +
                                        "intente mas tarde.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }else {
                        ArrayAdapter<String> adapter2 =
                                new ArrayAdapter<>(
                                        getContext(),
                                        R.layout.dropdown_menu_popup_item,
                                        menus);
                        editTextFilledExposedDropdown4.setAdapter(adapter2);
                        menuContainer.setVisibility(View.VISIBLE);
                    }
                }
            });

            String [] entrega;
            entrega = new String[] {"En sitio"};

            ArrayAdapter<String> adapter3 =
                    new ArrayAdapter<>(
                            getContext(),
                            R.layout.dropdown_menu_popup_item,
                            entrega);

            editTextFilledExposedDropdown2.setAdapter(adapter3);

            editTextFilledExposedDropdown4.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String [] dispodays;
                    Thread threadst;
                    final String menu =  editTextFilledExposedDropdown4.getText().toString();
                    final String dia = editTextFilledExposedDropdown.getText().toString().toLowerCase();
                    final StringBuffer response2 = new StringBuffer();
                    threadst = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                URL obj = new URL ( getString(R.string.server)+"chef/getdispomenu/"+empresaget+"/"+menu);
                                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                con.setRequestMethod("GET");
                                int responseCode = con.getResponseCode();
                                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                            con.getInputStream()));

                                    String inputLine;
                                    while ((inputLine = in.readLine()) != null) {
                                        response2.append(inputLine);
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
                    });threadst.start();
                    while(threadst.isAlive());
                    JSONArray arr = null;
                    List<String> list = new ArrayList<String>();
                    try {
                        arr = new JSONArray(response2.toString());
                        for(int i = 0; i < arr.length(); i++){
                            String str = arr.getJSONObject(i).getString(dia);
                            list.add(str);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dispodays = list.toArray(new String[0]);
                    if(Integer.parseInt(dispodays[0])==0){
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Lo Sentimos.")
                                .setMessage("No se encontró disponibilidad para el menu selecccionado, intente con otro " +
                                        "o intente mas tarde.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }else {
                        entregaContainer.setVisibility(View.VISIBLE);
                    }
                }
            });

            editTextFilledExposedDropdown2.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String[] hora;
                    if(s.toString().equals("En sitio")) {
                        dirContainer.setVisibility(View.GONE);
                        final String dia =  editTextFilledExposedDropdown.getText().toString().toLowerCase();
                        final String menu =  editTextFilledExposedDropdown4.getText().toString();
                        Thread threadnames;
                        final StringBuffer response4 = new StringBuffer();
                        threadnames = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    URL obj = new URL ( getString(R.string.server)+"chef/gethours/"+empresaget+"/"+dia+"/"+menu);
                                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                    con.setRequestMethod("GET");
                                    int responseCode = con.getResponseCode();
                                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                                con.getInputStream()));

                                        String inputLine;
                                        while ((inputLine = in.readLine()) != null) {
                                            response4.append(inputLine);
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
                        while(threadnames.isAlive());
                        JSONArray arr = null;
                        List<String> list = new ArrayList<String>();
                        try {
                            arr = new JSONArray(response4.toString());
                            System.out.println("myarray"+arr.toString());
                            for(int i = 0; i < arr.length(); i++){
                                String str = arr.getJSONObject(i).getString("horas");
                                list.add(str);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hora = list.toArray(new String[0]);

                        if(hora.length==0){
                            new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                    .setTitle("Lo Sentimos.")
                                    .setMessage("No se encontro disponibilidad para entrega en el sitio, intente con otro tipo " +
                                            "de entrega o intente mas tarde.")
                                    .setPositiveButton("Ok", null)
                                    .show();
                            editTextFilledExposedDropdown2.setText(null);
                        }else {
                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(
                                            getContext(),
                                            R.layout.dropdown_menu_popup_item,
                                            hora);
                            editTextFilledExposedDropdown3.setAdapter(adapter);
                            horaContainer.setVisibility(View.VISIBLE);
                        }

                    }else {
                        if(s.toString().equals("A domicilio")) {
                            horaContainer.setVisibility(View.GONE);
                            dirContainer.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String nombrestr = nombre.getText().toString();
                    final String celularstr = celular.getText().toString();
                    final String emailstr = email.getText().toString();
                    final String cargostr = cargo.getText().toString();
                    final String tipomenu = editTextFilledExposedDropdown4.getText().toString();
                    String dia = editTextFilledExposedDropdown.getText().toString();
                    if(dia.equalsIgnoreCase("Miércoles")){
                        dia = "Miercoles";
                    }
                    final String diastr = dia;
                    final String entrega = editTextFilledExposedDropdown2.getText().toString();
                    String hora2, direccion2;
                    if(entrega.equals("En sitio")){
                        hora2 = editTextFilledExposedDropdown3.getText().toString();
                        direccion2 = "";
                    }else {
                        direccion2 = direccion.getText().toString();
                        hora2 = "";
                    }
                    final String horaensitio = hora2;
                    final String direccionstr = direccion2;
                    final String observacionestr = observaciones.getText().toString();

                    if(tipomenu.equals("Alterno")){
                        Thread threadget = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    URL obj = new URL ( getString(R.string.server)+"chef/disponibilidad/"+empresaget+"/"+diastr);
                                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                                    con.setRequestMethod("GET");
                                    int responseCode = con.getResponseCode();
                                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
                                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                                con.getInputStream()));
                                        String inputLine;
                                        final StringBuffer response = new StringBuffer();

                                        while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                        }
                                        in.close();

                                        //PETICIONES
                                        if(!(response.toString().equalsIgnoreCase("0"))){
                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        URL url = new URL ( getString(R.string.server)+"chef/reserva/save/"+empresaget+diastr);
                                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                        conn.setDoOutput(true);
                                                        conn.setRequestMethod("POST");
                                                        conn.setRequestProperty("Content-Type", "application/json");

                                                        boolean lunes = false;
                                                        boolean martes = false;
                                                        boolean miercoles = false;
                                                        boolean jueves = false;
                                                        boolean viernes = false;

                                                        switch (diastr){
                                                            case "Lunes":{
                                                                lunes = true;
                                                            }
                                                            break;
                                                            case "Martes":{
                                                                martes = true;
                                                            }
                                                            break;
                                                            case "Miercoles":{
                                                                miercoles = true;
                                                            }
                                                            break;
                                                            case "Jueves":{
                                                                jueves = true;
                                                            }
                                                            break;
                                                            case "Viernes":{
                                                                viernes = true;
                                                            }
                                                            break;
                                                        }

                                                        String input = "{\"empresa\":\""+ empresasend +"\",\"fecha\":\"" + fecha + "\",\"hora\":\"" + hora + "\",\"nombre\":\"" + nombrestr + "\"" +
                                                                ",\"celular\":\""+celularstr+"\",\"correo\":\""+emailstr+"\",\"cargo\":\""+cargostr+"\",\"tipomenu\":\""+ tipomenu + "\"" +
                                                                ",\"lunes\":\""+lunes+"\",\"martes\":\""+martes+"\",\"miercoles\":\""+miercoles+"\",\"jueves\":\""+jueves+"\",\"viernes\":\""+
                                                                viernes+"\",\"entrega\":\""+entrega+"\",\"horaentrega\":\""+horaensitio+"\",\"direccion\":\""+direccionstr+"\","+
                                                                "\"observaciones\":\""+observacionestr+"\"}";

                                                        OutputStream os = conn.getOutputStream();
                                                        os.write(input.getBytes());
                                                        os.flush();

                                                        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                                            ReservaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                                                            .setTitle("Lo Sentimos.")
                                                                            .setMessage("Algo ocurrio al realizar la reserva, verifica tu conexion a internet " +
                                                                                    "o intentalo mas tarde.")
                                                                            .setPositiveButton("Ok", null)
                                                                            .show();
                                                                }
                                                            });
                                                        }else{
                                                            Thread thread3 = new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    int newdispoval = Integer.parseInt(response.toString()) -1;

                                                                    try{
                                                                        URL url2 = new URL ( getString(R.string.server)+"chef/disponibilidad/"+empresaget);
                                                                        HttpURLConnection httpCon = (HttpURLConnection) url2.openConnection();
                                                                        httpCon.setDoOutput(true);
                                                                        httpCon.setRequestMethod("PUT");
                                                                        httpCon.setRequestProperty("Content-Type", "application/json");
                                                                        OutputStreamWriter osw = new OutputStreamWriter(httpCon.getOutputStream());
                                                                        osw.write(String.format("{\"empresa\": \""+ empresasend + "\", \""+diastr+"\": "+newdispoval+"}"));
                                                                        osw.flush();
                                                                        osw.close();
                                                                        httpCon.getInputStream();
                                                                    }catch (MalformedURLException e){
                                                                        e.printStackTrace();
                                                                    }catch (IOException e){
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });thread3.start();

                                                            ReservaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                                public void run() {
                                                                    new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                                                            .setTitle("Reserva exitosa!")
                                                                            .setPositiveButton("Ok", null)
                                                                            .show();
                                                                }
                                                            });
                                                        }

                                                        conn.disconnect();

                                                    } catch (MalformedURLException e) {

                                                        e.printStackTrace();

                                                    } catch (IOException e) {

                                                        e.printStackTrace();

                                                    }
                                                }
                                            });

                                            thread.start();
                                        }else {
                                            ReservaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                                public void run() {
                                                    //Toast.makeText(ReservaFragment.this.getActivity(), "No hay reservas para ese dia!", Toast.LENGTH_LONG).show();
                                                    new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                                            .setTitle("Lo Sentimos.")
                                                            .setMessage("No se encontro disponibilidad para el tipo de menú selecccionado, intente con otro tipo " +
                                                                    "o intente mas tarde.")
                                                            .setPositiveButton("Ok", null)
                                                            .show();
                                                }
                                            });
                                        }

                                    } else {
                                        ReservaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                                        .setTitle("Lo Sentimos.")
                                                        .setMessage("Algo ocurrio al realizar la reserva, verifica tu conexion a internet " +
                                                                "o intentalo mas tarde.")
                                                        .setPositiveButton("Ok", null)
                                                        .show();
                                            }
                                        });
                                    }
                                }catch (MalformedURLException e){
                                    e.printStackTrace();
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        threadget.start();
                    }else{
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL ( getString(R.string.server)+"chef/reserva/save/"+empresaget+diastr);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setDoOutput(true);
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Content-Type", "application/json");

                                    boolean lunes = false;
                                    boolean martes = false;
                                    boolean miercoles = false;
                                    boolean jueves = false;
                                    boolean viernes = false;

                                    switch (diastr){
                                        case "Lunes":{
                                            lunes = true;
                                        }
                                        break;
                                        case "Martes":{
                                            martes = true;
                                        }
                                        break;
                                        case "Miercoles":{
                                            miercoles = true;
                                        }
                                        break;
                                        case "Jueves":{
                                            jueves = true;
                                        }
                                        break;
                                        case "Viernes":{
                                            viernes = true;
                                        }
                                        break;
                                    }

                                    String input = "{\"empresa\":\""+ empresasend +"\",\"fecha\":\"" + fecha + "\",\"hora\":\"" + hora + "\",\"nombre\":\"" + nombrestr + "\"" +
                                            ",\"celular\":\""+celularstr+"\",\"correo\":\""+emailstr+"\",\"cargo\":\""+cargostr+"\",\"tipomenu\":\""+ tipomenu + "\"" +
                                            ",\"lunes\":\""+lunes+"\",\"martes\":\""+martes+"\",\"miercoles\":\""+miercoles+"\",\"jueves\":\""+jueves+"\",\"viernes\":\""+
                                            viernes+"\",\"entrega\":\""+entrega+"\",\"horaentrega\":\""+horaensitio+"\",\"direccion\":\""+direccionstr+"\","+
                                            "\"observaciones\":\""+observacionestr+"\"}";

                                    OutputStream os = conn.getOutputStream();
                                    os.write(input.getBytes());
                                    os.flush();

                                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                        ReservaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                                        .setTitle("Lo Sentimos.")
                                                        .setMessage("Algo ocurrio al realizar la reserva, verifica tu conexion a internet " +
                                                                "o intentalo mas tarde.")
                                                        .setPositiveButton("Ok", null)
                                                        .show();
                                            }
                                        });
                                    }else{
                                        ReservaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                                        .setTitle("Reserva exitosa!")
                                                        .setPositiveButton("Ok", null)
                                                        .show();
                                            }
                                        });
                                    }

                                    conn.disconnect();

                                } catch (MalformedURLException e) {

                                    e.printStackTrace();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();
                    }
                }
            });
        }else{
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
        return root;
    }
}