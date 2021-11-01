package com.example.zpi.bottomnavigation.ui.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zpi.R;
import com.example.zpi.databinding.FragmentPlanBinding;
import com.example.zpi.models.Trip;
import com.example.zpi.models.TripPoint;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class PlanFragment extends Fragment {

    public final static String PLAN_KEY="PLAN";
    private Trip currTrip;
    private PlanViewModel planViewModel;
    private FragmentPlanBinding binding;
    private PlanRecyclerViewAdapter planRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        planViewModel = new ViewModelProvider(this).get(PlanViewModel.class);
        binding = FragmentPlanBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView planRV = binding.list;
        planRV.setLayoutManager(new LinearLayoutManager(getContext()));
        planRV.setHasFixedSize(true);

        PlanRecyclerViewAdapter planRecyclerViewAdapter = new PlanRecyclerViewAdapter(planViewModel.getTripPointList().getValue());
        planRV.setAdapter(planRecyclerViewAdapter);

        planViewModel.getTripPointList().observe(getViewLifecycleOwner(), new Observer<List<TripPoint>>() {
            @Override
            public void onChanged(List<TripPoint> tripPoints) {
                // update list
                Toast.makeText(getContext(), "Updated list", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void onPlanPointClick(int position){
        TripPoint point=planRecyclerViewAdapter.getTripPoint(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLAN_KEY, point);
        if(point.getTripPointType().equals("Atrakcja")){
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_plan_to_AttractonDetailsFragment, bundle);
        }else{
            Navigation.findNavController(getView()).navigate(R.id.action_navigation_plan_to_AccomodationDetailsFragment);
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //planRecyclerViewAdapter.deleteTodoPosition(viewHolder.getAbsoluteAdapterPosition());
        }
    };
}