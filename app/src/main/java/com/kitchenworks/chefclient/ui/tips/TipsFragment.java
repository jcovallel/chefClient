package com.kitchenworks.chefclient.ui.tips;

import android.content.ContextWrapper;
import android.content.res.Resources;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kitchenworks.chefclient.R;
import com.kitchenworks.chefclient.Second_activity;
import com.kitchenworks.chefclient.ui.menu.MenuFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class TipsFragment extends Fragment {

    private Bitmap [] mImages;
    private int imgnum;
    private String admin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Second_activity activity = (Second_activity) getActivity();
        admin = activity.getAdminName();
        imgnum = activity.getImgnumtips();

        View root = inflater.inflate(R.layout.fragment_tips, container, false);

        ContextWrapper contextWrapper = new ContextWrapper(TipsFragment.this.getActivity());
        BitmapFactory.Options bOptions = new BitmapFactory.Options();
        bOptions.inTempStorage = new byte[64*1024];
        mImages = new Bitmap[imgnum];

        for(int k=0; k<imgnum; k++){
            String ruta = contextWrapper.getFilesDir() +"/"+admin+"/"+ "Menu"+k+".jpg";
            Bitmap bitmap = BitmapFactory.decodeFile(ruta,bOptions);
            mImages[k]=bitmap;
        }

        CarouselView carouselView;
        if(TipsFragment.this.getActivity().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT){
            carouselView = root.findViewById(R.id.verticalTip);
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

        /*CarouselView carouselView;
        if(TipsFragment.this.getActivity().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT){
            //ImageView img = root.findViewById(R.id.verticalTip);
            //img.setImageResource(mImages);
            //img.setImageBitmap(getBitmapFromResources(getResources(), mImages));
            carouselView = root.findViewById(R.id.verticalTip);
            carouselView.setPageCount(mImages.length);
            carouselView.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setImageBitmap(getBitmapFromResources(getResources(),mImages[position]));
                }
            });
            carouselView.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                }
            });
        }else{
            int random = (int)(Math.random()*2)+1;
            ImageView img = root.findViewById(R.id.horizontalTip);
            if(random==1){
                img.setImageBitmap(getBitmapFromResources(getResources(), mImage));
            }else {
                mImage = R.mipmap.tip2;
                img.setImageBitmap(getBitmapFromResources(getResources(), mImage));
            }
        }*/

        /*if(nodispo){
            new MaterialAlertDialogBuilder(TipsFragment.this.getActivity())
                    .setTitle("Lo sentimos no se encontraron reservas disponibles, intente mas tarde.")
                    .setPositiveButton("Ok", null)
                    .show();
        }*/

        return root;
    }

    /*public static Bitmap getBitmapFromResources(Resources resources, int resImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 1;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeResource(resources, resImage, options);
    }*/
}