package com.example.finalproject.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.InventoryManager;
import com.example.finalproject.Item;
import com.example.finalproject.R;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    /**An Inventory Manager implemented by MainActivity.*/
    private InventoryManager invMan;

    /**Runs when this view is created by te navigation manager.
     * @param inflater Inflater allows inflation of root to get views from fragment_home xml.
     * @param container parent container for the root view.
     * @param savedInstanceState saved state, still not sure how to utilize.
     * @return the inflated root view. */
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //This button is for testing. on click, it adds a tree to the inventory.
        Button add = root.findViewById(R.id.addAThing);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //In the future, I want to make a list of everything at the beginning, and then just change the amt here.
                try {
                    Item tree = invMan.getItem("tree");
                    invMan.addToInventory(tree);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

    /** Pretty sure this is called when this fragment is attached to fragment view in MainActivity.
     * This function's purpose is to create the inventory manager based on MainActivity's implementation.
     * @param context main context */
    @Override
    public void onAttach(@NotNull final Context context) {
        super.onAttach(context);
        try {
            invMan = (InventoryManager) context;
        } catch (ClassCastException castException) {
            System.out.println("The activity does not implement the listener.");
            castException.printStackTrace();
        }
    }
}
