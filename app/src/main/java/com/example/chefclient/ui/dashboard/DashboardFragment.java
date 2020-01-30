package com.example.chefclient.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.chefclient.R;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private EditText fecha,hora,nombre,celular,email,cargo,observaciones;
    private Spinner dia;
    Button btnEnviar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        btnEnviar = (Button) root.findViewById(R.id.enviar);

        fecha = (EditText)root.findViewById(R.id.fechaRe);
        hora = (EditText)root.findViewById(R.id.horaRe);
        nombre = (EditText)root.findViewById(R.id.nombreRe);
        celular = (EditText)root.findViewById(R.id.celularRe);
        email = (EditText)root.findViewById(R.id.correoRe);
        cargo = (EditText)root.findViewById(R.id.CargoRe);
        dia = (Spinner)root.findViewById(R.id.diaRe);
        observaciones = (EditText)root.findViewById(R.id.ObservacionesRe);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechastr = fecha.getText().toString();
                String horastr = hora.getText().toString();
                String nombrestr = nombre.getText().toString();
                String celularstr = celular.getText().toString();
                String emailstr = email.getText().toString();
                String cargostr = cargo.getText().toString();
                String diastr = dia.getSelectedItem().toString();
                String observacionestr = observaciones.getText().toString();
                long numcelular = Long.parseLong(celularstr);

                try {

                    URL url = new URL("http://35.239.78.54:8080/reservacion/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");

                    String input = "{\"fecha\":\"" + fechastr + "\",\"hora\":\"" + horastr + "\",\"nombre\":\"" + nombrestr + "\"" +
                            ",\"celular\":\""+celularstr+"\",\"correo\":\""+emailstr+"\",\"cargo\":\""+cargostr+"\""+
                            ",\"day\":\""+diastr+"\",\"observaciones\":\""+observacionestr+"\"}";

                    OutputStream os = conn.getOutputStream();
                    os.write(input.getBytes());
                    os.flush();

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String output;
                    System.out.println("Output from Server .... \n");
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                    }

                    conn.disconnect();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        });
        /*final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        Spinner diaSp = (Spinner)root.findViewById(R.id.diaRe);
        String [] dias = {"lunes","martes","miercoles","jueves","viernes"};
        ArrayAdapter<String> diasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dias);
        diaSp.setAdapter(diasAdapter);

        return root;
    }
}