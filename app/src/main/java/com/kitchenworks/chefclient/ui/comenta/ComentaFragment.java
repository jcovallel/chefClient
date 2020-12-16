package com.kitchenworks.chefclient.ui.comenta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.kitchenworks.chefclient.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.kitchenworks.chefclient.Second_activity;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ComentaFragment extends Fragment {

    private ComentaViewModel comentaViewModel;
    private String empresa;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Second_activity activity = (Second_activity) getActivity();
        empresa = activity.getEmpresa();

        comentaViewModel = ViewModelProviders.of(this).get(ComentaViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_comenta, container, false);

        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        root.findViewById(R.id.imageView).setVisibility(View.GONE);
        root.findViewById(R.id.textView2).setVisibility(View.GONE);

        final RatingBar mRatingBar = (RatingBar) root.findViewById(R.id.ratingBar);
        final TextView mRatingScale = (TextView) root.findViewById(R.id.tvRatingScale);
        final TextInputEditText mFeedback = root.findViewById(R.id.ObservacionesRe);
        final TextInputEditText nombre = root.findViewById(R.id.Ratename);
        final TextInputEditText celular = root.findViewById(R.id.Ratephone);
        final TextInputEditText email = root.findViewById(R.id.Ratemail);

        Button mSendFeedback = (Button) root.findViewById(R.id.btnSubmit);


        /*mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });*/

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Mal");
                        break;
                    case 2:
                        mRatingScale.setText("Necesitan mejorar");
                        break;
                    case 3:
                        mRatingScale.setText("Bien");
                        break;
                    case 4:
                        mRatingScale.setText("Muy bien");
                        break;
                    case 5:
                        mRatingScale.setText("Excelente");
                        break;
                    default:
                        mRatingScale.setText("Muy Mal");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (mFeedback.getText().toString().isEmpty()) {
                    //Toast.makeText(ComentaFragment.this.getActivity(), "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                    //mFeedback.setText("");
                //}
                root.findViewById(R.id.scrollView).setVisibility(View.GONE);
                root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                root.findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                root.findViewById(R.id.textView2).setVisibility(View.VISIBLE);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL ( getString(R.string.server)+"chef/review/");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/json");
                            final String nombrestr = nombre.getText().toString();
                            final String celularstr = celular.getText().toString();
                            final String emailstr = email.getText().toString();
                            final String feed = mFeedback.getText().toString();

                            String input = "{\"empresa\":\""+empresa+"\",\"estrellas\":" + mRatingBar.getRating() + ",\"comentario\":\"" + feed + "\",\"nombre\":\"" +
                                           nombrestr + "\",\"celular\":\""+ celularstr + "\",\"correo\":\"" + emailstr + "\"}";

                            OutputStream os = conn.getOutputStream();
                            os.write(input.getBytes());
                            os.flush();

                            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                throw new RuntimeException("Failed : HTTP error code : "
                                        + conn.getResponseCode());
                            }else{
                                ComentaFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        new MaterialAlertDialogBuilder(ComentaFragment.this.getActivity())
                                                .setTitle("Gracias por sus comentarios!")
                                                .setPositiveButton("Ok", null)
                                                .show();
                                        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                        root.findViewById(R.id.imageView).setVisibility(View.GONE);
                                        root.findViewById(R.id.textView2).setVisibility(View.GONE);
                                        mFeedback.setText("");
                                        nombre.setText("");
                                        celular.setText("");
                                        email.setText("");
                                        mRatingBar.setRating(5);
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
        });

        return root;
    }
}