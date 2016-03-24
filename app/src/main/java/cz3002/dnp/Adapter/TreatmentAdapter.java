package cz3002.dnp.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz3002.dnp.ChangeTreatmentFragment;
import cz3002.dnp.Controller.TreatmentCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Treatment;
import cz3002.dnp.MainActivity;
import cz3002.dnp.R;

/**
 * Created by hizac on 26/2/2016.
 */
public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder> {
    private ArrayList<Treatment> treatments;
    public TreatmentAdapter(){
        treatments = TreatmentCtrl.getInstance().getTreatments();
    }
    @Override
    public TreatmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_in_list, parent, false);
        TreatmentViewHolder vh = new TreatmentViewHolder(mView);
        return vh;
    }

    @Override
    public void onBindViewHolder(TreatmentViewHolder holder, int position) {
        Treatment treatment = treatments.get(position);
        holder.setItem(treatment);
    }


    @Override
    public int getItemCount() {
        return treatments.size();
    }

    public class TreatmentViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        Treatment item;
        public TreatmentViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAnotherPage(item);
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            itemView.setBackgroundColor(Color.WHITE);
                            break;

                        case MotionEvent.ACTION_UP:
                            goToAnotherPage(item);
                            break;

                        default:
                            itemView.setBackgroundColor(Color.parseColor("#FF329593"));
                            break;
                    }

                    return true;
                }
            });
        }

        private void goToAnotherPage(Treatment item) {
            // Pass treatment to change_treatment fragment
            Bundle bundle = new Bundle();
            bundle.putString("treatmentId", String.format("%d", TreatmentCtrl.getInstance().getTreatments().indexOf(item)));
            ChangeTreatmentFragment changeTreatmentFragment = new ChangeTreatmentFragment();
            changeTreatmentFragment.setArguments(bundle);
            // Go to change_treatment fragment
            MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("treatmentlist").replace(R.id.main_container, changeTreatmentFragment).commit();
        }


        public void setItem(Treatment item) {
            this.item = item;
            TextView tvTime = (TextView)itemView.findViewById(R.id.time);
            // Reformat time
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String timeString = String.format("Treatment from %s to %s", format.format(item.getStartdate()), format.format(item.getEnddate()));
            tvTime.setText(timeString);

            TextView tvPartner = (TextView) itemView.findViewById(R.id.partner);
            String partnerString;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                partnerString = String.format("with patient %s", item.getPatient().getUsername());
            } else {
                partnerString = String.format("with Dr. %s",  item.getDoctor().getUsername());
            }
            tvPartner.setText(partnerString);

            TextView tvInfo = (TextView) itemView.findViewById(R.id.info);
            tvInfo.setText(item.getInfo());

        }
    }
}
