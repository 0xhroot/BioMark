// Generated by view binder compiler. Do not edit!
package com.example.biomark.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.biomark.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemAttendanceBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView itemEnrollment;

  @NonNull
  public final TextView itemInTime;

  @NonNull
  public final TextView itemName;

  @NonNull
  public final TextView itemOutTime;

  @NonNull
  public final TextView itemSection;

  @NonNull
  public final TextView itemStatus;

  private ItemAttendanceBinding(@NonNull CardView rootView, @NonNull TextView itemEnrollment,
      @NonNull TextView itemInTime, @NonNull TextView itemName, @NonNull TextView itemOutTime,
      @NonNull TextView itemSection, @NonNull TextView itemStatus) {
    this.rootView = rootView;
    this.itemEnrollment = itemEnrollment;
    this.itemInTime = itemInTime;
    this.itemName = itemName;
    this.itemOutTime = itemOutTime;
    this.itemSection = itemSection;
    this.itemStatus = itemStatus;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemAttendanceBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemAttendanceBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_attendance, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemAttendanceBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.item_enrollment;
      TextView itemEnrollment = ViewBindings.findChildViewById(rootView, id);
      if (itemEnrollment == null) {
        break missingId;
      }

      id = R.id.item_in_time;
      TextView itemInTime = ViewBindings.findChildViewById(rootView, id);
      if (itemInTime == null) {
        break missingId;
      }

      id = R.id.item_name;
      TextView itemName = ViewBindings.findChildViewById(rootView, id);
      if (itemName == null) {
        break missingId;
      }

      id = R.id.item_out_time;
      TextView itemOutTime = ViewBindings.findChildViewById(rootView, id);
      if (itemOutTime == null) {
        break missingId;
      }

      id = R.id.item_section;
      TextView itemSection = ViewBindings.findChildViewById(rootView, id);
      if (itemSection == null) {
        break missingId;
      }

      id = R.id.item_status;
      TextView itemStatus = ViewBindings.findChildViewById(rootView, id);
      if (itemStatus == null) {
        break missingId;
      }

      return new ItemAttendanceBinding((CardView) rootView, itemEnrollment, itemInTime, itemName,
          itemOutTime, itemSection, itemStatus);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
