package com.kitchenworks.chefclient.ui.menu;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.kitchenworks.chefclient.R;
import com.kitchenworks.chefclient.Second_activity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.apache.commons.lang3.StringUtils;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MenuFragment extends Fragment {

    private String empresasend;
    private int imgnum;
    private Bitmap [] mImages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Second_activity activity = (Second_activity) getActivity();
        empresasend = activity.getEmpresa();
        imgnum = activity.getImgnummenu();

        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        ContextWrapper contextWrapper = new ContextWrapper(MenuFragment.this.getActivity());
        String empresar = StringUtils.stripAccents(empresasend).replaceAll(" ","-");
        BitmapFactory.Options bOptions = new BitmapFactory.Options();
        bOptions.inTempStorage = new byte[64*1024];
        mImages = new Bitmap[imgnum];

        for(int k=0; k<imgnum; k++){
            String ruta = contextWrapper.getFilesDir() +"/"+empresar+"/"+ "Menu"+k+".jpg";
            Bitmap bitmap = BitmapFactory.decodeFile(ruta,bOptions);
            mImages[k]=bitmap;
        }

        CarouselView carouselView;
        if(MenuFragment.this.getActivity().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT){
            //ImageView img = root.findViewById(R.id.verticalTip);
            //img.setImageResource(mImages);
            //img.setImageBitmap(getBitmapFromResources(getResources(), mImages));
            carouselView = root.findViewById(R.id.verticalMenu);
            carouselView.setPageCount(imgnum);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(mImages[position]);
                }
            });
            carouselView.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                }
            });
        }
        /*ContextWrapper contextWrapper = new ContextWrapper(MenuFragment.this.getActivity());
        String empresar = StringUtils.stripAccents(empresasend).replaceAll(" ","-");
        //final String ruta = contextWrapper.getFilesDir() +"/"+ empresar +"/"+ "Menu.jpg";
        final String ruta = contextWrapper.getFilesDir() +"/prueba"+"/"+ "Menu0.jpg";

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

        }*/

        return root;
    }
}
