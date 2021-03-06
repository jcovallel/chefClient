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
import androidx.fragment.app.FragmentTransaction;
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

        final View root = inflater.inflate(R.layout.fragment_reserva, container, false);

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
        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        root.findViewById(R.id.textView2).setVisibility(View.GONE);

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

        Thread threadone2;
        final StringBuffer responsef2 = new StringBuffer();
        threadone2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL obj = new URL ( getString(R.string.server)+"chef/aredisponow/"+empresaget);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()));

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            responsef2.append(inputLine);
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
        });threadone2.start();
        while(threadone2.isAlive());

        final int dianumb = Integer.parseInt(new SimpleDateFormat("u", Locale.getDefault()).format(new Date()));

        try {
            arr = new JSONArray(responsef.toString());
            if((Boolean)arr.getJSONObject(0).get("lunes") && dianumb<=1){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Lunes");
                }else{
                    if(dianumb != 1){
                        list.add("Lunes");
                    }
                }
            }
            if((Boolean)arr.getJSONObject(0).get("martes") && dianumb<=2){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Martes");
                }else{
                    if(dianumb != 2){
                        list.add("Martes");
                    }
                }
            }
            if((Boolean)arr.getJSONObject(0).get("miercoles") && dianumb<=3){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Miercoles");
                }else{
                    if(dianumb != 3){
                        list.add("Miercoles");
                    }
                }
            }
            if((Boolean)arr.getJSONObject(0).get("jueves") && dianumb<=4){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Jueves");
                }else{
                    if(dianumb != 4){
                        list.add("Jueves");
                    }
                }
            }
            if((Boolean)arr.getJSONObject(0).get("viernes") && dianumb<=5){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Viernes");
                }else{
                    if(dianumb != 5){
                        list.add("Viernes");
                    }
                }
            }
            if((Boolean)arr.getJSONObject(0).get("sabado") && dianumb<=6){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Sabado");
                }else{
                    if(dianumb != 6){
                        list.add("Sabado");
                    }
                }
            }
            if((Boolean)arr.getJSONObject(0).get("domingo") && dianumb<=7){
                if(Boolean.parseBoolean(responsef2.toString())){
                    list.add("Domingo");
                }else{
                    if(dianumb != 7){
                        list.add("Domingo");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

            ss.setSpan(clickableSpan1,193,216, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
                    String selectedday = editTextFilledExposedDropdown.getText().toString();
                    int daynum = 0;
                    switch (selectedday){
                        case ("Lunes"):{
                            daynum = 1;
                        }break;
                        case ("Martes"):{
                            daynum = 2;
                        }break;
                        case ("Miercoles"):{
                            daynum = 3;
                        }break;
                        case ("Jueves"):{
                            daynum = 4;
                        }break;
                        case ("viernes"):{
                            daynum = 5;
                        }break;
                        case ("sabado"):{
                            daynum = 6;
                        }break;
                        case ("Domingo"):{
                            daynum = 7;
                        }break;
                    }
                    if(daynum==dianumb){
                        Thread threadst;
                        final StringBuffer response2 = new StringBuffer();
                        threadst = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    URL obj = new URL ( getString(R.string.server)+"chef/getmenusnowempresa/"+empresaget);
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
                    }else {
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
                    if(!s.toString().equals("")){
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
                            editTextFilledExposedDropdown4.setText("");
                        }else {
                            entregaContainer.setVisibility(View.VISIBLE);
                        }
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
                //@Override
                public void onClick(View v) {
                    Boolean error=false;
                    final String nombrestr = nombre.getText().toString();
                    if(nombrestr.isEmpty()){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe proporcionar un nombre.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    final String celularstr = celular.getText().toString();
                    if(celularstr.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe proporcionar un numero de teléfono.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    final String emailstr = email.getText().toString();
                    if(emailstr.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe proporcionar un email.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    final String cargostr = cargo.getText().toString();
                    if(cargostr.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe proporcionar un cargo.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    String dia = editTextFilledExposedDropdown.getText().toString();
                    final String diastr = dia;
                    if(diastr.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe seleccionar un día.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    final String tipomenu = editTextFilledExposedDropdown4.getText().toString();
                    if(tipomenu.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe seleccionar un menu.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    final String entrega = editTextFilledExposedDropdown2.getText().toString();
                    if(entrega.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe seleccionar el tipo de entrega.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    String hora2, direccion2;
                    if(entrega.equals("En sitio")){
                        hora2 = editTextFilledExposedDropdown3.getText().toString();
                        direccion2 = "";
                    }else {
                        direccion2 = direccion.getText().toString();
                        hora2 = "";
                    }
                    final String horaensitio = hora2;
                    if(horaensitio.isEmpty() && !error){
                        error=true;
                        new MaterialAlertDialogBuilder(ReservaFragment.this.getActivity())
                                .setTitle("Error.")
                                .setMessage("Debe seleccionar un horario de entrega.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                    final String direccionstr = direccion2;
                    final String observacionestr = observaciones.getText().toString();

                    if(!error){
                        root.findViewById(R.id.scrollView).setVisibility(View.GONE);
                        root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                        root.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL ( getString(R.string.server)+"chef/reserva/save/"+empresaget+diastr);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setDoOutput(true);
                                    conn.setRequestMethod("POST");
                                    conn.setRequestProperty("Content-Type", "application/json");

                                    String input = "{\"empresa\":\""+ empresasend +"\",\"fecha\":\"" + fecha + "\",\"hora\":\"" + hora + "\",\"nombre\":\"" + nombrestr + "\"" +
                                            ",\"celular\":\""+celularstr+"\",\"correo\":\""+emailstr+"\",\"cargo\":\""+cargostr+"\",\"tipomenu\":\""+ tipomenu + "\"" +
                                            ",\"dia\":\""+diastr+"\",\"entrega\":\""+entrega+"\",\"horaentrega\":\""+horaensitio+"\","+
                                            "\"observaciones\":\""+observacionestr+"\",\"plataforma\":\"APP\"}";

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
                                                root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                                root.findViewById(R.id.imageView).setVisibility(View.GONE);
                                                root.findViewById(R.id.textView2).setVisibility(View.GONE);
                                                nombre.setText("");
                                                celular.setText("");
                                                email.setText("");
                                                cargo.setText("");
                                                editTextFilledExposedDropdown.setText("");
                                                editTextFilledExposedDropdown2.setText("");
                                                editTextFilledExposedDropdown3.setText("");
                                                editTextFilledExposedDropdown4.setText("");
                                                menuContainer.setVisibility(View.GONE);
                                                horaContainer.setVisibility(View.GONE);
                                                dirContainer.setVisibility(View.GONE);
                                                entregaContainer.setVisibility(View.GONE);
                                                observaciones.setText("");
                                                btnEnviar.setEnabled(false);
                                                root.findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
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