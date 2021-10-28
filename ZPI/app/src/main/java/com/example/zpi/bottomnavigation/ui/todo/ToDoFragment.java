package com.example.zpi.bottomnavigation.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zpi.R;
import com.example.zpi.data_handling.BaseConnection;
import com.example.zpi.databinding.FragmentToDoBinding;
import com.example.zpi.models.PreparationPoint;
import com.example.zpi.models.ProductToTake;
import com.example.zpi.models.Trip;
import com.example.zpi.repositories.PreparationPointDao;
import com.example.zpi.repositories.ProductToTakeDao;
import com.example.zpi.repositories.TripDao;

import java.sql.SQLException;
import java.util.List;


public class ToDoFragment extends Fragment implements TodoRecyclerViewAdapter.OnTodoListener {

    private ToDoViewModel toDoViewModel;
    private FragmentToDoBinding binding;
    private TodoRecyclerViewAdapter todoRecyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        binding = FragmentToDoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.addButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_todo_to_addToDoFragment));

        RecyclerView todoRV = binding.recyclerViewToDo;
        todoRV.setLayoutManager(new LinearLayoutManager(getContext()));
        todoRV.setHasFixedSize(true);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(todoRV);

        new Thread(() -> {
            try {
                Trip trip = new TripDao(BaseConnection.getConnectionSource()).queryForEq("ID", 1).get(0);
                List<PreparationPoint> todos = new PreparationPointDao(BaseConnection.getConnectionSource()).getPreparationPointsByTrip(trip);

                Log.i("todo size fragemnt", String.valueOf(todos.size()));
                getActivity().runOnUiThread(() -> {
                    todoRecyclerViewAdapter = new TodoRecyclerViewAdapter(todos, this);
                    todoRV.setAdapter(todoRecyclerViewAdapter);
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).start();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onTodoClick(int position) {
        Log.i("todo click", "clicked:" + position);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            todoRecyclerViewAdapter.deleteTodoPosition(viewHolder.getAbsoluteAdapterPosition());
        }
    };

}