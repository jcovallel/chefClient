package com.kitchenworks.chefclient.ui.tips;

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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.Calendar;
import java.util.Date;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class TipsFragment extends Fragment {

    private TipsViewModel tipsViewModel;
    private int [] mImages;
    private  int mImage;
    private boolean nodispo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Second_activity activity = (Second_activity) getActivity();
        nodispo = activity.getDispo();

        tipsViewModel =
                ViewModelProviders.of(this).get(TipsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tips, container, false);

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int month = cal.get(Calendar.MONTH)+1;

        switch (month){
            case 1:{
                /*
                mImages = new int[] {
                        R.mipmap.ene1, R.mipmap.ene2
                };*/
            }break;
            case 2:{
                mImages = new int[] {
                        R.mipmap.feb, R.mipmap.tip2
                };
                mImage = R.mipmap.feb;
            }break;
            case 3:{
                mImages = new int[] {
                        R.mipmap.mar, R.mipmap.tip2
                };
                mImage = R.mipmap.mar;
            }break;
            case 4:{
                mImages = new int[] {
                        R.mipmap.abr, R.mipmap.tip2
                };
                mImage = R.mipmap.abr;
            }break;
            case 5:{
                mImages = new int[] {
                        R.mipmap.may, R.mipmap.tip2
                };
                mImage = R.mipmap.may;
            }break;
            case 6:{
                mImages = new int[] {
                        R.mipmap.jun, R.mipmap.tip2
                };
                mImage = R.mipmap.jun;
            }break;
            case 7:{
                mImages = new int[] {
                        R.mipmap.jul, R.mipmap.tip2
                };
                mImage = R.mipmap.jul;
            }break;
            case 8:{
                mImages = new int[] {
                        R.mipmap.ago, R.mipmap.tip2
                };
                mImage = R.mipmap.ago;
            }break;
            case 9:{
                mImages = new int[] {
                        R.mipmap.sep, R.mipmap.tip2
                };
                mImage = R.mipmap.sep;
            }break;
            case 10:{
                mImages = new int[] {
                        R.mipmap.oct, R.mipmap.tip2
                };
                mImage = R.mipmap.oct;
            }break;
            case 11:{
                mImages = new int[] {
                        R.mipmap.nov, R.mipmap.tip2
                };
                mImage = R.mipmap.nov;
            }break;
            case 12:{
                mImages = new int[] {
                        R.mipmap.dic, R.mipmap.tip2
                };
                mImage = R.mipmap.dic;
            }break;
        }

        CarouselView carouselView;
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
        }

        /*if(nodispo){
            new MaterialAlertDialogBuilder(TipsFragment.this.getActivity())
                    .setTitle("Lo sentimos no se encontraron reservas disponibles, intente mas tarde.")
                    .setPositiveButton("Ok", null)
                    .show();
        }*/

        return root;
    }

    public static Bitmap getBitmapFromResources(Resources resources, int resImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inSampleSize = 1;
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeResource(resources, resImage, options);
    }
}