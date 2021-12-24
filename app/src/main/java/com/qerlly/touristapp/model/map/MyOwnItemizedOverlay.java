package com.qerlly.touristapp.model.map;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.qerlly.touristapp.R;
import com.qerlly.touristapp.model.TourPoint;
import com.qerlly.touristapp.repositories.TourRepository;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.List;

import timber.log.Timber;

public class MyOwnItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

    protected Context mContext;

    private final TourRepository tourRepository;

    private final TourPoint tourPoint;

    public MyOwnItemizedOverlay(final Context context, TourRepository tourRepository, TourPoint tourPoint,  final List<OverlayItem> aList) {
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
        this.tourRepository = tourRepository;
        this.tourPoint = tourPoint;
        mContext = context;
    }

    @Override
    protected boolean onSingleTapUpHelper(final int index, final OverlayItem item, final MapView mapView) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        String[] arr = item.getSnippet().split("\t");

        boolean isGid = arr[2].endsWith("@firma.gid.com");

        if (Long.parseLong(arr[1]) == 2L) dialog.setTitle(item.getTitle().concat(" - is passed!"));
        else if (Long.parseLong(arr[1]) == 1L) dialog.setTitle(item.getTitle().concat(" - is current!"));
        else dialog.setTitle(item.getTitle());

        dialog.setMessage(arr[0]);

        if(Long.parseLong(arr[1]) == 2L) dialog.setIcon(R.drawable.checked);


        dialog.setPositiveButton("Ok", (dialog1, id) -> dialog1.dismiss());

        if (isGid) {
            dialog.setNegativeButton("Make current",
                    (dialog13, id) -> tourRepository.setPointCurrent(tourPoint));
            dialog.setNeutralButton("Make passed",
                    (dialog12, id) -> tourRepository.setPointPassed(tourPoint));
        }
        dialog.show();
        return true;
    }



    @Override
    public void onDetach(MapView mapView) {
        mItemList = null;
        super.onDetach(mapView);
    }
}
