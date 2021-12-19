package com.qerlly.touristapp.model.map;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.qerlly.touristapp.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

public class MyOwnItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

    protected Context mContext;

    public MyOwnItemizedOverlay(final Context context, final List<OverlayItem> aList) {
        super(context, aList, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                return false;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }
        });
        mContext = context;
    }

    @Override
    protected boolean onSingleTapUpHelper(final int index, final OverlayItem item, final MapView mapView) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        String[] arr = item.getSnippet().split("\t");

        if (Boolean.parseBoolean(arr[1])) dialog.setTitle(item.getTitle().concat(" - is passed!"));
        else dialog.setTitle(item.getTitle());

        dialog.setMessage(arr[0]);

        if(Boolean.parseBoolean(arr[1])) dialog.setIcon(R.drawable.checked);

        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }



    @Override
    public void onDetach(MapView mapView) {
        mItemList = null;
        super.onDetach(mapView);
    }
}
