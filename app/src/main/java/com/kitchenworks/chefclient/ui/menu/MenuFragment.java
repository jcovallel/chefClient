package com.kitchenworks.chefclient.ui.menu;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.kitchenworks.chefclient.MainActivity;
import com.kitchenworks.chefclient.R;
import com.kitchenworks.chefclient.Second_activity;

import org.apache.commons.lang3.StringUtils;

public class MenuFragment extends Fragment {

    private MenuViewModel menuViewModel;
    private String empresasend,empresaget;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Second_activity activity = (Second_activity) getActivity();
        empresaget = activity.getEmpresa().replaceAll(" ", "%20");
        empresasend = activity.getEmpresa();

        menuViewModel = ViewModelProviders.of(this).get(MenuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        ContextWrapper contextWrapper = new ContextWrapper(MenuFragment.this.getActivity());
        String empresar = StringUtils.stripAccents(empresasend).replaceAll(" ","-");
        final String ruta = contextWrapper.getFilesDir() +"/"+ empresar+ "Menu.jpg";

        ConnectivityManager connectivityManager = (ConnectivityManager) MenuFragment.this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        MainActivity mainActivity= new MainActivity();
        //System.out.println(mainActivity.thread.isAlive());

        if(networkInfo.isConnected()){
            //ContentLoadingProgressBar loadingProgressBar = root.findViewById(R.id.loading);
            //while (mainActivity.thread.isAlive()){
            //    loadingProgressBar.show();
            //}
            //loadingProgressBar.setVisibility(View.INVISIBLE);
            BitmapFactory.Options bOptions = new BitmapFactory.Options();
            bOptions.inTempStorage = new byte[64*1024];
            Bitmap bitmap = BitmapFactory.decodeFile(ruta,bOptions);
            ImageView tip = root.findViewById(R.id.menuimg);
            tip.setImageBitmap(bitmap);
        }else{

        }

        return root;
    }
}
