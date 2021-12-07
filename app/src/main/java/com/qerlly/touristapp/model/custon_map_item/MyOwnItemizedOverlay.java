package com.qerlly.touristapp.model.custon_map_item;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

public class MyOwnItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

    protected Context mContext;

    public MyOwnItemizedOverlay(final Context context, final List<OverlayItem> aList) {
        super(context, aList, new OnItemGestureListener<OverlayItem>() {
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
        //Toast.makeText(mContext, "Item " + index + " has been tapped!", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }
}